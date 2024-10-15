package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import data.CrewData;
import exceptions.NotFoundException;
import models.UserAccount;
import java.sql.ResultSet;

public class CrewRole {
	private String id;
	private String crew_id;
	private String name;

	public CrewRole(String id, String crew_id, String name) {
		this.id = id;
		this.crew_id = crew_id;
		this.name = name;
	}

	public String getId() { return this.id; }

	public String getCrew_id() { return this.crew_id; }

	public String getName() { return this.name; }

	public String getAccountId(Connection con, CrewData crewData) throws SQLException, NotFoundException{
		String query = "SELECT id FROM user_account WHERE username = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, crewData.getUserName());
		ResultSet rs = stmt.executeQuery();
		if(!rs.next()) 
			throw new NotFoundException("User acount id is not existed!");
		return rs.getString("id");
	}

	public int getCrewId(Connection con, CrewData crewData) throws SQLException, NotFoundException{
		String query = "SELECT id FROM crew WHERE name = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, crewData.getUserName());
		ResultSet rs = stmt.executeQuery();
		
		if (!rs.next()) {
		throw new NotFoundException("Crew ID is not existed!");
		}
		return rs.getInt("id");
	}

	public int getCrewRoleId(Connection con, CrewData crewData) throws SQLException, NotFoundException{
		crewData.setCrewRoleId(getCrewId(con, crewData));
		String query = "SELECT * FROM crew_role WHERE crew_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, crewData.getCrewRoleId());
		ResultSet rs = stmt.executeQuery();
		if(!rs.next()) 	
			throw new NotFoundException("Crew role id is not existed!");
		return rs.getInt("id");
	}

	public void insertCrewMember(Connection con, CrewData crewData) throws SQLException, NotFoundException{
		String account_id = getAccountId(con, crewData);
		int crew_role_id = getCrewRoleId(con, crewData);
		String query = "INSERT INTO user_role(account_id, crew_role_id) VALUES (?, ?)";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);
		stmt.setInt(2, crew_role_id);
		int row = stmt.executeUpdate();
		if(row == 0)
			throw new SQLException("Insert failured");
	}

	public void updateCrewMember(Connection con, CrewData crewData, int newCrewRoleId) throws SQLException, NotFoundException{
		String account_id = getAccountId(con, crewData);
		int crew_role_id = getCrewRoleId(con, crewData);
		String query = "UPDATE user_role SET crew_role_id = ? WHERE account_id = ? AND crew_role_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, newCrewRoleId);
		stmt.setString(2, account_id);
		stmt.setInt(3, crew_role_id);
		int row = stmt.executeUpdate();
		if(row == 0)
			throw new SQLException("Update failured");
	} 

	public void deleteCrewMember(Connection con, CrewData crewData) throws SQLException, NotFoundException{
		int crew_role_id = getCrewRoleId(con, crewData);
		String account_id = getAccountId(con, crewData);
		String query = "DELETE FROM user_role WHERE crew_role_id = ? AND account_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, crew_role_id);
		stmt.setString(2, account_id);
		int row = stmt.executeUpdate();
		if(row == 0)
			throw new SQLException("Delete failured");
	} 




}
