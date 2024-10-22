package controllers;

import data.GuildData;
import models.Guild;
import models.roles.GuildRole;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GuildController {
    public static void add(Connection connection, String userName, String guildName, String guildRole) {
        try {
            GuildData guildData = new GuildData(userName, guildName, guildRole);
            GuildRole.insertGuildMember(connection, guildData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void delete(Connection connection, String userName, String guildName, String guildRole) {
        try {
            GuildData guildData = new GuildData(userName, guildName, guildRole);
            GuildRole.deleteCrewMember(connection, guildData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void update(Connection connection, String userName, String guildName, String guildRole) {
        try {
            GuildData guildData = new GuildData(userName, guildName, guildRole);
            GuildRole.deleteCrewMember(connection, guildData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String> getAllGuilds(Connection connection) throws SQLException {
        return Guild.getAllNameToList(connection);
    }
}
