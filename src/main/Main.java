import config.AppConfig;
import config.Database;
import dto.SignUpDTO;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;
import repositories.users.UserAccountRepository;
import repositories.users.UserRoleRepository;
import utils.EnvLoader;
import views.AuthView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static services.AuthService.hashingPassword;

public class Main {
	public static void main(String[] args) {
		try {
			Connection con = Database.connection();
			create_first_account(con);
			AuthView view = new AuthView(con);
			view.Auth_view();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void create_first_account(Connection con) throws SQLException, DataEmptyException, IOException,
			ClassNotFoundException, NotFoundException {

		try {
			SignUpDTO firstAccount = new SignUpDTO();
			AppConfig config = EnvLoader.load(AppConfig.class);
			firstAccount.setUsername(config.getAdminUsername());
			firstAccount.setPassword(hashingPassword(config.getAdminPassword(), config.getRoundHashing()));
			UserAccountRepository.insert(firstAccount);
			String adminId = UserAccountRepository.getIdByUsername(firstAccount.getUsername());
			UserRoleRepository.insert(adminId, 1);
		} catch (SQLException e) {
			System.out.println("Admin account is created");
		}
	}

}
