package services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import at.favre.lib.crypto.bcrypt.BCrypt;
import data.ClaimsData;
import data.LoginData;
import data.SignUpData;
import data.TokenPairData;
import exceptions.AuthException;
import exceptions.DataEmptyException;
import exceptions.InvalidPasswordException;
import exceptions.NotFoundException;
import exceptions.TokenException;
import models.UserAccount;

public class AuthService {
	public static void signUpInternal(Connection con, SignUpData data)
			throws InvalidPasswordException, AuthException, DataEmptyException, SQLException,
			SQLIntegrityConstraintViolationException {

		int round = Integer.parseInt(Optional.ofNullable(System.getenv("ROUND_HASHING")).orElse("4"));
		String[] errorsPassword = AuthService.validatePassword(data.getPassword());

		if (errorsPassword.length != 0)
			for (String tmp : errorsPassword)
				throw new InvalidPasswordException(tmp);

		if (data.getUsername() == null || data.getUsername() == "")
			throw new IllegalArgumentException("Your username musn't be empty!");

		data.setPassword(hashingPassword(data.getPassword(), round));
		UserAccount.insert(con, data);
	}

	public static void loginInternal(Connection con, LoginData data)
			throws AuthException, TokenException, SQLException, NotFoundException {
		PreparedStatement stmt = con.prepareStatement(
				"SELECT username, hashed_password FROM user_account WHERE username = ?");
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
				ClaimsData claimsData = new ClaimsData(rs.getString("username"));
				TokenPairData tokenData = TokenPairData.GenerateNew(claimsData);
				TokenService.saveToFile(path, tokenData);
			}
		}
		System.out.println("Login successfully!");
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

	public static void Authorization() {
	}
}
