package services;

import java.sql.Connection;
import java.sql.PreparedStatement;

import data.LoginData;
import data.SignUpData;
import models.UserAccount;

public class AuthService {
	public static void signUpInternal(Connection con, SignUpData data) {
		try {
			UserAccount.insert(con, data);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void loginInternal(Connection con, LoginData data) {
		try {
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
