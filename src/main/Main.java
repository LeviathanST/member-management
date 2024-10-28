import java.sql.Connection;

import constants.ResponseStatus;
import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;
import dto.UserProfileDTO;
import views.AuthView;
import views.UserProfileView;

public class Main {
	public static void main(String[] args) {
		try {
			LoginDTO log_in = new LoginDTO();
			SignUpDTO sign_up = new SignUpDTO();
			UserProfileDTO user_profile = new UserProfileDTO();
			Connection con = Database.connection();
			AuthView view = new AuthView(con);
			UserProfileView user_profile_view = new UserProfileView(con);
			ResponseDTO<Object> response = view.Auth_view(sign_up, log_in);
			if(response.getMessage() == "Sign up successfully!") {
				ResponseDTO<Object> response_profile;
				do {
					response_profile = user_profile_view.insertUserProfile(con, user_profile, sign_up);
					System.out.println(response_profile.getMessage());
				} while (response_profile.getStatus() != ResponseStatus.OK);
			}
			user_profile_view.ReadUserProfile(con, user_profile, log_in);
			System.out.println(user_profile.getFullName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
