package config;

import java.io.IOException;
import java.sql.*;

import utils.EnvLoader;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection connection() throws IOException, SQLException, ClassNotFoundException {
        DbConfig dbConfig = EnvLoader.load(DbConfig.class);
        Class.forName("com.mysql.cj.jdbc.Driver");
        String db_url = dbConfig.getDbUrl();
        String db_password = dbConfig.getDbPassword();
        Connection connection = DriverManager.getConnection(
                db_url,
                "root",
                db_password);
        return connection;
    }
}
