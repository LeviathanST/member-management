package services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.auth0.jwt.algorithms.Algorithm;

import at.favre.lib.crypto.bcrypt.BCrypt;
import data.ClaimsData;
import data.LoginData;
import data.SignUpData;
import data.TokenPairData;
import exceptions.AuthException;
import models.UserAccount;

public class AuthService {
	public static void signUpInternal(Connection con, SignUpData data) throws AuthException{
		try {
			int round = Optional.ofNullable(Integer.parseInt(System.getenv("ROUND_HASHING"))).orElse(1);
			data.setPassword(UserAccount.hasingPassword(data.getPassword(), round));
			UserAccount.insert(con, data);
		} catch (Exception e) {
			System.out.println("Error occurs in sign up : "  + e.getMessage());
		} 
	}



	public static void loginInternal(Connection con, LoginData data) throws AuthException {
		try {
			PreparedStatement stmt = con.prepareStatement(
					"SELECT username, hashed_password FROM user_account WHERE username = ?");
			stmt.setString(1, data.getUsername());

			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				throw new AuthException(
						"Your username is not existed, please sign up with this username or login with other account!");
			} else {
				BCrypt.Result result = BCrypt
						.verifyer()
						.verify(data.getPassword().toCharArray(),
								rs.getString("hashed_password"));

				if (!result.verified) {
					throw new AuthException("Wrong password!");
				} else {
					Path path = Paths.get("cache.json");
					ClaimsData claimsData = new ClaimsData(rs.getString("username"));

					TokenPairData tokenData = TokenPairData.GenerateNew(claimsData);

					System.out.println(tokenData.getAccessToken());
					TokenService.saveToFile(path, tokenData);
				}
			}
			System.out.println("Login successfully!");

		} catch (SQLException e) {
			throw new AuthException("Error occurs when querying in login: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new AuthException("Error occurs when login: " + e.getMessage(), e);
		}
	}
}
