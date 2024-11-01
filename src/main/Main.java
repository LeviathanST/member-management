import java.sql.Connection;

import models.permissions.Permission;
import models.users.UserAccount;
import services.AuthService;
import views.AuthView;

public class Main {
	public static void main(String[] args) {
		try {
			Connection con = Database.connection();
			AuthView view = new AuthView(con);
			view.Auth_view();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
