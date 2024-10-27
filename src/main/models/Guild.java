package models;

import dto.GuildDTO;
import exceptions.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	public static void insert(Connection con, GuildDTO guildDTO) throws SQLException, NotFoundException {
		String query = "INSERT INTO guild (name) VALUES (?)";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, guildDTO.getName());
		int rowsAffected = stmt.executeUpdate();
		if (rowsAffected == 0){
			throw new SQLException("Insert failed!");
		}
	}
	public static void update(Connection con, GuildDTO guildDTO) throws SQLException, NotFoundException {
		int id = getIdByName(con, guildDTO.getName());
		String query = "UPDATE guild SET name = ? WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, guildDTO.getName());
		stmt.setInt(2, id);
		int rowsAffected = stmt.executeUpdate();
		if (rowsAffected == 0){
			throw new SQLException("Update failed!");
		}
	}
	public static void delete(Connection con, GuildDTO guildDTO) throws SQLException, NotFoundException {
		int id = getIdByName(con, guildDTO.getName());
		String query = "DELETE FROM guild WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, id);
		int rowsAffected = stmt.executeUpdate();
		if (rowsAffected == 0){
			throw new SQLException("Delete failed!");
		}
	}
}
