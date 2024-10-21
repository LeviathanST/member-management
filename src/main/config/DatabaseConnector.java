package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private String url;
    private String username;
    private String password;
    private String driver;
    private int port;

    public DatabaseConnector(ConfigLoader configLoader) {
        this.url = configLoader.getProperty("db.url");
        this.username = configLoader.getProperty("db.username");
        this.password = configLoader.getProperty("db.password");
        this.driver = configLoader.getProperty("db.driver");
        this.port = Integer.parseInt(configLoader.getProperty("db.port"));
    }

    public Connection connect() {
        Connection connection = null;
        try {
            Class.forName(driver);
            String url = String.format("jdbc:mysql://localhost:%d/member-management", port);
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database successfully!");
        } catch (ClassNotFoundException | SQLException | IllegalArgumentException  e) {
            e.printStackTrace();
        }
        return connection;
    }

}

