import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
	public static void main(String[] args) {
		String db_user = "root";
		String db_password = "admin";
		// int round = Optional.ofNullable(Integer.parseInt(System.getenv("ROUND_HASHING"))).orElse(1);
		String port = System.getenv("PORT");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:" + port + "/member-management",
					db_user,
					db_password);

			System.out.println("Connection successfully!");

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
