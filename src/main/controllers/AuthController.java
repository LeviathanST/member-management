package controllers;

import java.sql.Connection;
import data.LoginData;
import data.SignUpData;
import services.AuthService;

public class AuthController {
	public void signUp(Connection con, SignUpData data) {
		try {
			AuthService.signUpInternal(con, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login(Connection con, LoginData data) {
		try {
			AuthService.loginInternal(con, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
