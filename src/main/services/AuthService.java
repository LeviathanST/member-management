package services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import at.favre.lib.crypto.bcrypt.BCrypt;
import constants.RoleType;
import dto.ClaimsDTO;
import dto.LoginDTO;
import dto.SignUpDTO;
import dto.TokenPairDTO;
import exceptions.*;
import models.users.UserAccount;
import models.users.UserCrewRole;
import models.users.UserGuildRole;
import models.users.UserRole;
import models.permissions.CrewPermission;
import models.permissions.GuildPermission;
import models.permissions.Permission;
import models.roles.Role;

public class AuthService {
	public static void signUpInternal(Connection con, SignUpDTO data)
			throws AuthException, SQLException, DataEmptyException, NotFoundException {

		int round = Integer.parseInt(Optional.ofNullable(System.getenv("ROUND_HASHING")).orElse("4"));
		String[] errorsPassword = AuthService.validatePassword(data.getPassword());
		String errors = "";
		
		if (data.getUsername() == null || data.getUsername() == "" || data.getUsername().contains(" "))
			errors += "Your username musn't be empty or contains space!\n";

		if (errorsPassword.length != 0) {
			for (String tmp : errorsPassword)
				errors += tmp + "\n";
		}

		if (ApplicationService.isValidEmail(data.getEmail()) == false)
			errors += "Invalid email!";

		if(errors != "")
			throw new AuthException(errors);

		data.setPassword(hashingPassword(data.getPassword(), round));
		UserAccount.insert(con, data);
		String account_id = UserAccount.getIdByUsername(con, data.getUsername());
		int role_id = Role.getByName(con, "Member").getId();
		UserRole.insert(con, account_id, role_id);
	}

	public static void loginInternal(Connection con, LoginDTO data)
			throws AuthException, TokenException, SQLException, NotFoundException {
		PreparedStatement stmt = con.prepareStatement(
				"SELECT id, hashed_password FROM user_account WHERE username = ?");
		stmt.setString(1, data.getUsername());

		ResultSet rs = stmt.executeQuery();
		if (!rs.next()) {
			throw new NotFoundException(
					"Your username is not existed, please sign up with this username or login with other account!");
		} else {
			BCrypt.Result result = BCrypt
					.verifyer()
					.verify(data.getPassword().toCharArray(),
							rs.getString("hashed_password"));

			if (!result.verified) {
				throw new AuthException("Wrong password!");
			} else {
				Path path = Paths.get("auth.json");
				String account_id = rs.getString("id");

				int userRoleId = UserRole.getIdByAccountId(con, account_id);
				List<Integer> userGuildRoleId = UserGuildRole.getIdByAccountId(con, account_id);
				List<Integer> userCrewRole = UserCrewRole.getIdByAccountId(con, account_id);

				ClaimsDTO claimsData = new ClaimsDTO(account_id, userRoleId, userGuildRoleId,
						userCrewRole);
				TokenPairDTO tokenData = TokenPairDTO.GenerateNew(claimsData);
				TokenService.saveToFile(path, tokenData);
			}
		}
	}

	private static String hashingPassword(String password, int round) {
		String bcryptHashing = BCrypt.withDefaults()
				.hashToString(round, password.toCharArray());
		return bcryptHashing;
	}

	private static String[] validatePassword(String password) {
		// List to hold validation error messages
		java.util.List<String> errors = new java.util.ArrayList<>();

		if (password == null) {
			errors.add("Password cannot be null.");
			return errors.toArray(new String[0]);
		}

		if (password.length() <= 8) {
			errors.add("Password must be longer than 8 characters.");
		}

		if (!password.matches(".*[A-Z].*")) {
			errors.add("Password must contain at least one uppercase letter.");
		}

		if (!password.matches(".*[a-z].*")) {
			errors.add("Password must contain at least one lowercase letter.");
		}

		if (!password.matches(".*\\d.*")) {
			errors.add("Password must contain at least one digit.");
		}

		if (!password.matches(".*[@#$%^&+=!].*")) {
			errors.add("Password must contain at least one special character (@#$%^&+=!).");
		}

		return errors.toArray(new String[0]);
	}

	public static boolean AppAuthorization(Connection con, 
			String namePermission) throws SQLException, NotFoundException, TokenException {
		return Authorization(con,  0, RoleType.Application, namePermission);
	}

	public static boolean GuildAuthorization(Connection con, int guildId,
			String namePermission) throws SQLException, NotFoundException, TokenException {
		return Authorization(con, guildId, RoleType.Guild, namePermission);
	}

	public static boolean CrewAuthorization(Connection con, int crewId,
			String namePermission) throws SQLException, NotFoundException, TokenException {
		return Authorization(con, crewId, RoleType.Crew, namePermission);
	}

	/// ID
	/// + crew_id
	/// + guild_id
	/// if using for application let id = 0
	public static boolean Authorization(Connection con, int id, RoleType type,
			String namePermission)
			throws NotFoundException, SQLException, TokenException {
		boolean isAuthorized;
		Path path = (Path)Paths.get("auth.json");
		String accessToken = TokenService.loadFromFile(path).getAccessToken();
		String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id").toString();
		try {
			switch (type) {
				case RoleType.Guild -> {
					List<GuildPermission> guildPermissions = GuildPermission
							.getAllByAccountIdAndGuildId(con,
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
							.getAllByAccountIdAndGuildId(con,
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
					List<Permission> permissions = Permission.getByAccountId(con, accountId);

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
