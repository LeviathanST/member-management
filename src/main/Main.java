import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

public class Main {
	public static void main(String[] args) {
		String db_user = "root";
		String db_password = "admin";
		int port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("3306"));
		String db_url = String.format("jdbc:mysql://localhost:%d/member-management", port);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(
					db_url,
					db_user,
					db_password);

			System.out.println("Connection successfully!");

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
