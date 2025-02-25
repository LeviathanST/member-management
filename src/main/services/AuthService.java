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
import jakarta.servlet.http.Cookie;
import models.CrewPermission;
import models.Permission;
import models.UserProfile;
import repositories.GuildRepository;
import repositories.CrewRepository;
import repositories.PermissionRepository;
import repositories.RoleRepository;
import repositories.users.UserAccountRepository;
import repositories.users.UserProfileRepository;
import repositories.GenerationRepository;
import repositories.users.UserRoleRepository;
import utils.EnvLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

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
		RoleRepository.addDefaultForUserByPrefix(data.getUsername(), "%\\", "NOT LIKE");
		Logger logger = LoggerFactory.getLogger(AuthService.class);
		logger.debug("Added role for " + data.getUsername());
		UserProfileRepository.insert(
				data.getUsername(),
				Sex.NONE,
				GenerationRepository.getMax(),
				Date.valueOf(LocalDate.now()));
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

	public static String hashingPassword(String password, int round) {
		String bcryptHashing = BCrypt.withDefaults()
				.hashToString(round, password.toCharArray());
		return bcryptHashing;
	}

	/// @param f:
	/// a function that will be received access token as parameter
	/// then run Consumer if cond = true
	public static String handleCookieAndGetAccountId(Cookie[] cookies, Boolean cond, Consumer<String> f)
			throws AuthException {
		for (Cookie cookie : cookies) {
			if ("access_token".equals(cookie.getName())) {
				String accessToken = cookie.getValue();
				String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id")
						.asString();
				f.accept(accessToken);
				return accountId;
			}
		}

		throw new AuthException("Your credentials is not found!");
	}

	public static String handleCookieAndGetAccountId(Cookie[] cookies) throws AuthException {
		for (Cookie cookie : cookies) {
			if ("access_token".equals(cookie.getName())) {
				String accessToken = cookie.getValue();
				String accountId = TokenPairDTO.Verify(accessToken).getClaim("account_id")
						.asString();
				return accountId;
			}
		}

		throw new AuthException("Your credentials is not found!");
	}

	public static boolean checkPermissionWithContext(String accountId, RoleContext ctx,
			List<String> permissions)
			throws SQLException, AuthException, NotFoundException {
		List<String> list = new ArrayList<>(Collections.singletonList("*"));
		list.addAll(permissions);
		Boolean checked = RoleRepository.existPermissionWithContext(ctx,
				list,
				accountId);
		return checked;
	}

	/// NOTE:
	/// This method is used for check specified guild, crew role in user_role
	/// @param name:
	/// - It can be guild or crew name which is determined by @param ctx.
	/// Sample: BE, BE1, FE, FE1
	/// - User will be bypassed all if have '*' permission
	/// HACK: It will be return false when guild, crew or specified level not found!
	/// TODO: Making exception can be more context
	/// - NOT FOUND GUILD, CREW
	/// - NOT FOUND SPECIFIED LEVEL OF CREW
	public static boolean checkRoleAndPermission(String accountId, String name, RoleContext ctx,
			String permission)
			throws SQLException, AuthException, NotFoundException {
		String prefix = switch (ctx) {
			case RoleContext.GUILD -> GuildRepository.getCodeByName(name);
			case RoleContext.CREW -> CrewRepository.getCodeByName(name);
			default -> "Not valid with your code";
		};
		boolean god = RoleRepository.existPermissionWithContext(ctx, "*", accountId);
		boolean checked = RoleRepository.existPermissionWithPrefix(prefix, ctx,
				permission,
				accountId);
		return checked || god;
	}

}
