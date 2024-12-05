package services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import config.AppConfig;
import constants.RoleType;
import dto.ClaimsDTO;
import dto.LoginDTO;
import dto.SignUpDTO;
import dto.TokenPairDTO;
import exceptions.*;
import models.permissions.CrewPermission;
import models.permissions.GuildPermission;
import models.permissions.Permission;
import models.roles.Role;
import models.users.UserAccount;
import models.users.UserCrewRole;
import models.users.UserGuildRole;
import models.users.UserRole;
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
            SQLIntegrityConstraintViolationException, NotFoundException, IOException, ClassNotFoundException {
        
		AppConfig appConfig = EnvLoader.load(AppConfig.class);
		int round = appConfig.getRoundHashing();
		data.setPassword(hashingPassword(data.getPassword(), round));
		UserAccount.insert( data);
		String account_id = UserAccount.getIdByUsername( data.getUsername());
		int role_id = Role.getByName("Member").getId();
		UserRole.insert(account_id, role_id);
	}

	public static void loginInternal(LoginDTO data)
            throws AuthException, TokenException, SQLException, NotFoundException, IOException, ClassNotFoundException {
		String hashPassword = UserAccount.getHashPasswordByUsername( data);
		BCrypt.Result result = BCrypt
					.verifyer()
					.verify(data.getPassword().toCharArray(), hashPassword);
		if (!result.verified) {
			throw new AuthException("Wrong password!");
		} else {
			Path path = Paths.get("storage.json");
			String account_id = UserAccount.getIdByUsername( data.getUsername());
			int userRoleId = UserRole.getIdByAccountId( account_id);
			List<Integer> userGuildRoleId = UserGuildRole.getIdByAccountId( account_id);
			List<Integer> userCrewRole = UserCrewRole.getIdByAccountId( account_id);


			ClaimsDTO claimsData = new ClaimsDTO(account_id, userRoleId, userGuildRoleId,
						userCrewRole);
			TokenPairDTO tokenData = TokenPairDTO.GenerateNew(claimsData);
			TokenService.saveToFile(path, tokenData);
		}

	}

	public static void changeAccessToken(SignUpDTO data) throws TokenException, SQLException, NotFoundException, IOException, ClassNotFoundException {
		Path path = (Path)Paths.get("storage.json");
		String accountId = UserAccount.getIdByUsername( data.getUsername());
		List<Integer> userGuildRoleId = UserGuildRole.getIdByAccountId( accountId);
			List<Integer> userCrewRole = UserCrewRole.getIdByAccountId( accountId);
			ClaimsDTO claimsData = new ClaimsDTO(accountId, 2, userGuildRoleId, userCrewRole);
		TokenPairDTO tokenData = TokenPairDTO.GenerateNew(claimsData);
		TokenService.saveToFile(path, tokenData);
	}

	public static boolean checkAccessToken() throws TokenException, SQLException, IOException, ClassNotFoundException {
		Path path = (Path)Paths.get("storage.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
		List<String> list = UserAccount.getAllId();
		for(String i : list)
			if(i.equals(accountId))
				return true;
		return false;
	}

	public static String hashingPassword(String password, int round) {
		String bcryptHashing = BCrypt.withDefaults()
				.hashToString(round, password.toCharArray());
		return bcryptHashing;
	}



	public static boolean AppAuthorization(
			String namePermission) throws SQLException, NotFoundException, TokenException, IOException, ClassNotFoundException {
		return Authorization(0, RoleType.Application, namePermission);
	}

	public static boolean GuildAuthorization(int guildId,
			String namePermission) throws SQLException, NotFoundException, TokenException, IOException, ClassNotFoundException {
		return Authorization(guildId, RoleType.Guild, namePermission);
	}

	public static boolean CrewAuthorization(int crewId,
			String namePermission) throws SQLException, NotFoundException, TokenException, IOException, ClassNotFoundException {
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
		Path path = (Path)Paths.get("storage.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
		try {
			switch (type) {
				case RoleType.Guild -> {
					List<GuildPermission> guildPermissions = GuildPermission
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
					List<CrewPermission> crewPermissions = CrewPermission
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
					List<Permission> permissions = Permission.getByAccountId( accountId);
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
