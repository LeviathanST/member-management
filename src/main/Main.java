import config.ConfigLoader;
import config.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

public class Main {
	public static void main(String[] args) {
		String configFilePath = "./src/main/config/db-config.properties";
		ConfigLoader configLoader = new ConfigLoader(configFilePath);
		DatabaseConnector databaseConnector = new DatabaseConnector(configLoader);
		databaseConnector.connect();
	}

}
