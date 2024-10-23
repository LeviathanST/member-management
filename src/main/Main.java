import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;
import java.util.Scanner;

import controllers.AuthController;
import data.LoginData;
import data.ProfileData;
import data.SignUpData;
import views.*;



public class Main {
	public static void main(String[] args) {
		AuthView view = new AuthView();
		ShowMenu menu = new ShowMenu();
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
			int choice = view.SignUp_LogIn();
			view.clearScreen();
			int check = 0;
			switch (choice) {
				case 1:
					SignUpData signUpData = new SignUpData();
					view.signUpForm(signUpData);
					AuthController.signUp(connection, signUpData);
					view.clearScreen("Press any key to continue : ");
					check = 1;
					break;
				case 2 : 
					LoginData loginData = new LoginData();
					view.LogInForm(loginData);
					AuthController.login(connection, loginData);
					view.clearScreen("Press any key to continue : ");
					check = 1;
					break;
				default:
					view.invalidValue();
					break;
			}
			if(check == 1) {
				int choices = menu.showMenu();
				switch (choices) {
					case 1:
						UserProfileView profileView = new UserProfileView();
						ProfileData data = new ProfileData();
						profileView.ProfileView(data);
						break;
				
					default:
						break;
				}
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
