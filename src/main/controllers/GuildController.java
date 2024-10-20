package controllers;

import models.Guild;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GuildController {
    public static void add(Connection connection, String userName, String guildName, String guildRole) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void delete(Connection connection, String userName, String guildName) {}
    public static void update(Connection connection, String userName, String guildName) {}
    public static List<String> getAllGuilds(Connection connection) throws SQLException {
        return Guild.getAllNameToList(connection);
    }
}
