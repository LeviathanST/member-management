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
	private int id;

	public Generation(int id) {
		this.id = id;
	}

	public static void insert( int id) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "INSERT INTO generation (id) VALUES (?)";

			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, id);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Insert %d to Generation is failed!", id));
			}
		}

	}

	public static void update(int oldId, int newId) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "UPDATE generation SET id = ? WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, newId);
			stmt.setInt(2, oldId);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(
						String.format("Update %d to %d in generation is failed!", oldId, newId));
			}
		}
	}

	public static void delete( int id) throws SQLException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "DELETE FROM generation WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, id);

			int rowEffected = stmt.executeUpdate();
			if (rowEffected == 0) {
				throw new SQLException(String.format("Delete %d from Generation is failed!", id));
			}
		}
	}

	public static String getNameById( int id) throws SQLException, IOException, ClassNotFoundException {
		String query = "SELECT id FROM generation WHERE id = ?";
		try (Connection con = Database.connection();
			PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
				if (!rs.next()) {
					throw new ClassNotFoundException("Can not find generation id.");
				}
				StringBuilder builder = new StringBuilder();
				builder.append("F");
				builder.append(rs.getInt("id"));
				return builder.toString();
		}
	}

	public static List<String> getAllGenerations() throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "SELECT * FROM generation";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			List<String> generations = new ArrayList<>();
			while (rs.next()) {
				StringBuilder stringBulder = new StringBuilder();
				stringBulder.append("F");
				stringBulder.append(rs.getInt("id"));
				generations.add(stringBulder.toString());
			}
			return generations;
		}
	}
	public static List<Integer> getAllGeneration() throws SQLException, NotFoundException, IOException, ClassNotFoundException {
		try(Connection con = Database.connection()) {
			String query = "SELECT * FROM generation";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			List<Integer> generations = new ArrayList<>();
			while (rs.next()) {
				generations.add(rs.getInt("id"));
			}
			return generations;
		}
	}
}
