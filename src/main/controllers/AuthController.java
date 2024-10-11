package controllers;

import java.sql.Connection;

import data.SignUpData;
import models.UserAccount;

public class AuthController {
	public static void signUp(Connection con, SignUpData data) {
		try {
			UserAccount.insert(con, data);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void login(Connection con, SignUpData data) {

	}
}
