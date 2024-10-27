package models;

import dto.CrewDTO;
import exceptions.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Crew {
	private String name;

	public Crew(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void getAllName(Connection con) throws SQLException {
		String query = "SELECT name FROM crew ";
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("name"));
		}
	}

	public static int getIdByName(Connection con, String name) throws SQLException, NotFoundException {
		String query = "SELECT id FROM crew WHERE name = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			return rs.getInt("id");
		}

		throw new NotFoundException("Crew ID is not existed!");
	}

	public static List<String> getAllNameToList(Connection con) throws SQLException {
		List<String> crewNames = new ArrayList<>();
		String query = "SELECT name FROM crew ";
		PreparedStatement stmt = con.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			crewNames.add(rs.getString("name"));
		}
		return crewNames;
	}
	public static void insert (Connection con, CrewDTO crewDTO) throws SQLException, NotFoundException {
		String query = "INSERT INTO crew (name) VALUES (?)";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, crewDTO.getName());
		int rowsAffected = stmt.executeUpdate();
		if (rowsAffected == 0) {
			throw new NotFoundException("Insert failed!");
		}
	}
	public static void update(Connection con, CrewDTO crewDTO) throws SQLException, NotFoundException {
		int id = getIdByName(con, crewDTO.getName());
		String query = "UPDATE crew SET name = ? WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, crewDTO.getName());
		stmt.setInt(2, id);
		int rowsAffected = stmt.executeUpdate();
		if (rowsAffected == 0) {
			throw new NotFoundException("Update failed!");
		}
	}
	public static void delete(Connection con, CrewDTO crewDTO) throws SQLException, NotFoundException {
		int id = getIdByName(con, crewDTO.getName());
		String query = "DELETE FROM crew WHERE id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, id);
		int rowsAffected = stmt.executeUpdate();
		if (rowsAffected == 0) {
			throw new NotFoundException("Delete failed!");
		}
	}
}
