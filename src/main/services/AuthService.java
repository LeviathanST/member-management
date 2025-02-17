package services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import config.AppConfig;
import constants.RoleContext;
import constants.RoleType;
import constants.Sex;
import dto.ClaimsDTO;
import dto.LoginDTO;
import dto.SignUpDTO;
import dto.TokenPairDTO;
import exceptions.*;
import models.CrewPermission;
import models.Generation;
import models.GuildPermission;
import models.Permission;
import models.UserProfile;
import repositories.permissions.CrewPermissionRepository;
import repositories.permissions.GuildPermissionRepository;
import repositories.permissions.PermissionRepository;
import repositories.roles.RoleRepository;
import repositories.users.UserAccountRepository;
import repositories.users.UserProfileRepository;
import repositories.users.UserRoleRepository;
import utils.EnvLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		Logger logger = LoggerFactory.getLogger(AuthService.class);
		logger.info("Before");
		UserProfile userProfile = new UserProfile(
				account_id,
				"Empty",
				Sex.NONE,
				"Empty",
				"Empty",
				"Empty",
				ApplicationService.getMaxGenerationId(),
				Date.valueOf(LocalDate.now()));
		UserProfileRepository.insert(userProfile);
		logger.info("After");
		UserRoleRepository.insert(account_id, role_id);
	}

	// Update for refresh token
	public static String loginInternal(LoginDTO data)
			throws AuthException, TokenException, SQLException, NotFoundException, IOException,
			ClassNotFoundException, DataEmptyException {
		if (data.getPassword().trim().isEmpty() || data.getUsername().trim().isEmpty()) {
			throw new DataEmptyException("Your username or password is empty");
		}
		String hashPassword = UserAccountRepository.getHashPasswordByUsername(data);
		BCrypt.Result result = BCrypt
				.verifyer()
				.verify(data.getPassword().toCharArray(), hashPassword);
		if (!result.verified) {
			throw new AuthException("Wrong password!");
		} else {
			String account_id = UserAccountRepository.getIdByUsername(data.getUsername());
			int userRoleId = UserRoleRepository.getIdByAccountId(account_id);

			ClaimsDTO claimsData = new ClaimsDTO(account_id, userRoleId);
			TokenPairDTO tokenData = TokenPairDTO.GenerateNew(claimsData);
			return tokenData.getAccessToken();
		}

	}

	public static void changeAccessToken(SignUpDTO data)
			throws TokenException, SQLException, NotFoundException, IOException, ClassNotFoundException {
		Path path = (Path) Paths.get("storage.json");
		String accountId = UserAccountRepository.getIdByUsername(data.getUsername());
		ClaimsDTO claimsData = new ClaimsDTO(accountId, 2);
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

	// TODO: Authenticate user with multiple roles and multiple context
	public static boolean CheckPermissionWithContext(RoleContext ctx, String permission, String accessToken)
			throws SQLException {
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").asString();
		boolean checked = RoleRepository.existPermissionWithContext(ctx, permission, accountId);
		return checked;
	}
}
