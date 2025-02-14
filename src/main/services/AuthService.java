package services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import config.AppConfig;
import constants.RoleType;
import dto.ClaimsDTO;
import dto.LoginDTO;
import dto.SignUpDTO;
import dto.TokenPairDTO;
import exceptions.*;
import models.CrewPermission;
import models.GuildPermission;
import models.Permission;
import repositories.permissions.CrewPermissionRepository;
import repositories.permissions.GuildPermissionRepository;
import repositories.permissions.PermissionRepository;
import repositories.roles.RoleRepository;
import repositories.users.UserAccountRepository;
import repositories.users.UserCrewRoleRepository;
import repositories.users.UserGuildRoleRepository;
import repositories.users.UserRoleRepository;
import utils.EnvLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class AuthService {
	public static void signUpInternal(SignUpDTO data)
			throws InvalidPasswordException, AuthException, DataEmptyException, SQLException,
			SQLIntegrityConstraintViolationException, NotFoundException, IOException,
			ClassNotFoundException {
		if (data.getPassword().trim().isEmpty() || data.getUsername().trim().isEmpty()) {
			throw new DataEmptyException("Your username or password is empty");
		}

		AppConfig appConfig = EnvLoader.load(AppConfig.class);
		int round = appConfig.getRoundHashing();
		data.setPassword(hashingPassword(data.getPassword(), round));
		UserAccountRepository.insert(data);
		String account_id = UserAccountRepository.getIdByUsername(data.getUsername());
		int role_id = RoleRepository.getByName("Member").getId();
		UserRoleRepository.insert(account_id, role_id);
	}

	public static void loginInternal(LoginDTO data)
			throws AuthException, TokenException, SQLException, NotFoundException, IOException,
			ClassNotFoundException {
		String hashPassword = UserAccountRepository.getHashPasswordByUsername(data);
		BCrypt.Result result = BCrypt
				.verifyer()
				.verify(data.getPassword().toCharArray(), hashPassword);
		if (!result.verified) {
			throw new AuthException("Wrong password!");
		} else {
			String account_id = UserAccountRepository.getIdByUsername(data.getUsername());
			int userRoleId = UserRoleRepository.getIdByAccountId(account_id);
			List<Integer> userGuildRoleId = UserGuildRoleRepository.getIdByAccountId(account_id);
			List<Integer> userCrewRole = UserCrewRoleRepository.getIdByAccountId(account_id);

			ClaimsDTO claimsData = new ClaimsDTO(account_id, userRoleId, userGuildRoleId,
					userCrewRole);
			// TODO: save token
			TokenPairDTO tokenData = TokenPairDTO.GenerateNew(claimsData);
		}

	}

	public static void changeAccessToken(SignUpDTO data)
			throws TokenException, SQLException, NotFoundException, IOException, ClassNotFoundException {
		Path path = (Path) Paths.get("storage.json");
		String accountId = UserAccountRepository.getIdByUsername(data.getUsername());
		List<Integer> userGuildRoleId = UserGuildRoleRepository.getIdByAccountId(accountId);
		List<Integer> userCrewRole = UserCrewRoleRepository.getIdByAccountId(accountId);
		ClaimsDTO claimsData = new ClaimsDTO(accountId, 2, userGuildRoleId, userCrewRole);
		TokenPairDTO tokenData = TokenPairDTO.GenerateNew(claimsData);
		TokenService.saveToFile(path, tokenData);
	}

	public static boolean checkAccessToken()
			throws TokenException, SQLException, IOException, ClassNotFoundException {
		Path path = (Path) Paths.get("storage.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
		List<String> list = UserAccountRepository.getAllId();
		for (String i : list)
			if (i.equals(accountId))
				return true;
		return false;
	}

	public static String hashingPassword(String password, int round) {
		String bcryptHashing = BCrypt.withDefaults()
				.hashToString(round, password.toCharArray());
		return bcryptHashing;
	}

	public static boolean AppAuthorization(
			String namePermission)
			throws SQLException, NotFoundException, TokenException, IOException, ClassNotFoundException {
		return Authorization(0, RoleType.Application, namePermission);
	}

	public static boolean GuildAuthorization(int guildId,
			String namePermission)
			throws SQLException, NotFoundException, TokenException, IOException, ClassNotFoundException {
		return Authorization(guildId, RoleType.Guild, namePermission);
	}

	public static boolean CrewAuthorization(int crewId,
			String namePermission)
			throws SQLException, NotFoundException, TokenException, IOException, ClassNotFoundException {
		return Authorization(crewId, RoleType.Crew, namePermission);
	}

	/// ID
	/// + crew_id
	/// + guild_id
	/// if using for application let id = 0
	public static boolean Authorization(int id, RoleType type,
			String namePermission)
			throws NotFoundException, SQLException, TokenException, IOException, ClassNotFoundException {
		boolean isAuthorized;
		Path path = (Path) Paths.get("storage.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
		try {
			switch (type) {
				case RoleType.Guild -> {
					List<GuildPermission> guildPermissions = GuildPermissionRepository
							.getAllByAccountIdAndGuildId(
									accountId, id);

					isAuthorized = false;
					if (guildPermissions.isEmpty()) {
						throw new NotFoundException(
								"This account is not have needed permission!");
					}
					for (GuildPermission permission : guildPermissions) {
						System.out.println(permission.getName());
						if (permission.getName().equals(namePermission)) {
							isAuthorized = true;
						}
					}
					break;
				}
				case RoleType.Crew -> {
					List<CrewPermission> crewPermissions = CrewPermissionRepository
							.getAllByAccountIdAndCrewId(
									accountId, id);

					isAuthorized = false;
					if (crewPermissions.isEmpty()) {
						throw new NotFoundException(
								"This account is not have needed permission!");
					}
					for (CrewPermission permission : crewPermissions) {
						if (permission.getName().equals(namePermission)) {
							isAuthorized = true;
						}
					}
					break;
				}
				case RoleType.Application -> {
					List<Permission> permissions = PermissionRepository
							.getByAccountId(accountId);
					isAuthorized = false;
					for (Permission permission : permissions) {
						if (permission.getName().equals(namePermission)) {
							isAuthorized = true;
						}
					}
					break;
				}
				default -> throw new NotFoundException("Authorization: Your permission is not found!");
			}
			return isAuthorized;
		} catch (SQLException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (NotFoundException e) {
			throw new NotFoundException(e.getMessage());
		}
	}
}
