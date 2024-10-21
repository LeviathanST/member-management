import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

import controllers.AuthController;
import data.SignUpData;
import views.*;



public class Main {
	public static void main(String[] args) {
		AuthView view = new AuthView();
		String db_user = "root";
		String db_password = "admin";
		int port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("3308"));
		String db_url = String.format("jdbc:mysql://localhost:%d/member-management", port);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(
					db_url,
					db_user,
					db_password);
			System.err.println("Connection Successfully!");
			int choice = view.SignUp_LogIn();
			switch (choice) {
				case 1:
				SignUpData signUpData = new SignUpData();
					view.signUpForm(signUpData);
					AuthController.signUp(connection, signUpData);
					break;
				case 2 : 

					break;
				default:
					view.invalidValue();
					break;
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
