package controllers;

import data.CrewData;
import models.Crew;
import models.roles.CrewRole;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CrewController {
    public static void add(Connection connection, String userName, String crewName, String crewRole) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void delete(Connection connection, String userName, String crewName, String crewRole) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void update(Connection connection, String userName, String crewName, String crewRole) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String> getAllCrews(Connection connection) throws SQLException {
        return Crew.getAllNameToList(connection);
    }
}
