package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import exceptions.NotFoundException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Guild {
	private String name;

	public Guild(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void getAllName(Connection con) throws SQLException {
		String query = "SELECT name FROM guild ";
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("name"));
		}
	}

	public static int getIdByName(Connection con, String name) throws SQLException, NotFoundException {
		String query = "SELECT id FROM guild WHERE name = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			return rs.getInt("id");
		}

		throw new NotFoundException("Guild ID is not existed!");
	}

	public static List<String> getAllNameToList(Connection con) throws SQLException {
		List<String> guildNames = new ArrayList<>();
		String query = "SELECT name FROM guild ";
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			guildNames.add(rs.getString("name"));
		}
		return guildNames;
	}
}
