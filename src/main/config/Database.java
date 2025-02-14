package config;

import java.io.IOException;
import java.sql.*;

import utils.EnvLoader;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;

public class Database {
    public static Connection connection() throws RuntimeException {
        try {
            DbConfig dbConfig = EnvLoader.load(DbConfig.class);
            Class.forName("com.mysql.cj.jdbc.Driver");
            String db_path = String.format("jdbc:mysql://%s/%s", dbConfig.getDbUrl(), dbConfig.getDbDatabase());
            Connection connection = DriverManager.getConnection(
                    db_path,
                    "root",
                    dbConfig.getDbPassword());
            return connection;
        } catch (Exception e) {
            Logger logger = org.slf4j.LoggerFactory.getLogger(Database.class);
            logger.error(e.getMessage());
            e.printStackTrace();

            throw new RuntimeException(e.getMessage());
        }

    }
}
