package controllers;

import java.nio.file.Paths;
import java.sql.Connection;

import constants.RoleType;
import data.LoginData;
import data.SignUpData;
import data.TokenPairData;
import models.Guild;
import models.roles.GuildRole;
import services.AuthService;
import services.TokenService;

public class AuthController {
	public static void signUp(Connection con, SignUpData data) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void login(Connection con, LoginData data) {
		try {
			AuthService.loginInternal(con, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
