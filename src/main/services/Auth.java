package services;

import java.sql.Connection;

import models.SignUpData;
import models.UserAccount;

public class Auth {
	public static void SignUp(Connection con, SignUpData data) {
		try {
			UserAccount.Insert(con, data);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
