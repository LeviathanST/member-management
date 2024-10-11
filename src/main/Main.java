import java.sql.Connection;
import java.sql.DriverManager;

import models.SignUpData;
import services.Auth;

public class Main {
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/member-management",
					"root",
					"admin");

			SignUpData data = new SignUpData("hung", "hungdeptrai");

			Auth.SignUp(connection, data);

			System.out.println("Connection successfully!");

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
