import controllers.AuthController;
import data.SignUpData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		String db_user = "root";
		String db_password = "admin";
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection connection = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/member-management",
						db_user,
						db_password);

				System.out.println("Connection successfully!");
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
