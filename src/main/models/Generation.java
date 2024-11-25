package models;

import config.Database;
import exceptions.NotFoundException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Generation {
	private String name;

	public Generation(String name) {
		this.name = name;
	}

	public static void insert( String name) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "INSERT INTO generation (name) VALUES (?)";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Insert %s to Generation is failed!", name));
			}
		}

	}

	public static void update(String oldName, String newName) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "UPDATE generation SET name = ? WHERE name = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, newName);
			stmt.setString(2, oldName);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Update %s to %s in generation is failed!", oldName, newName));
			}
		}
	}

	public static void delete( String name) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "DELETE FROM generation WHERE name = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Delete %s from Generation is failed!", name));
			}
		}
	}
	public static int getIdByName( String name) throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "SELECT id FROM generation WHERE name = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("id");
			}
			throw new NotFoundException("Generation ID is not existed!");
		}

	}
	public static String getNameById( int id) throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "SELECT name FROM generation WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("name");
			}

			throw new NotFoundException("Generation name is not existed!");
		}

	}
	public static List<String> getAllGenerations() throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "SELECT * FROM generation";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			List<String> generations = new ArrayList<>();
			while (rs.next()) {
				generations.add(rs.getString("name"));
			}
			return generations;
		}


	}

}
