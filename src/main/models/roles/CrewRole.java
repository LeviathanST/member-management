package models.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import data.CrewData;
import exceptions.DeleteFailure;
import exceptions.InsertFailure;
import exceptions.NotFoundException;
import exceptions.UpdateFailure;
import models.UserAccount;
import java.sql.ResultSet;

public class CrewRole {
	private String id;
	private String crew_id;
	private String name;

	// TODO:
	// public static CrewRole getByAccountId(Connection con, String account_id) {
		
	// }

	public CrewRole(String id, String crew_id, String name) {
		this.id = id;
		this.crew_id = crew_id;
		this.name = name;
	}

	public String getId() { return this.id; }

	public String getCrew_id() { return this.crew_id; }

	public String getName() { return this.name; }

	public String getAccountId(Connection con, CrewData crewData) throws SQLException, NotFoundException{
		String query = "SELECT * FROM user_account WHERE username = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, crewData.getUserName());
		ResultSet rs = stmt.executeQuery();
		if(!rs.next()) 
			throw new NotFoundException("User acount id is not existed!");
		return rs.getString("id");
	}

	public int getCrewId(Connection con, CrewData crewData) throws SQLException, NotFoundException{
		String query = "SELECT * FROM crew WHERE name = ?";
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

	public void insertCrewMember(Connection con, CrewData crewData) throws SQLException, InsertFailure, NotFoundException{
		String account_id = getAccountId(con, crewData);
		int crew_role_id = getCrewRoleId(con, crewData);
		String query = "INSERT INTO user_role(account_id, crew_role_id) VALUES (?, ?)";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);
		stmt.setInt(2, crew_role_id);
		int row = stmt.executeUpdate();
		if(row == 0)
			throw new InsertFailure("Insert failured");
	}

	public void updateCrewMember(Connection con, CrewData crewData, String newCrewRoleId) throws SQLException, NotFoundException, UpdateFailure{
		String account_id = getAccountId(con, crewData);
		int crew_role_id = getCrewRoleId(con, crewData);
		String query = "UPDATE user_role SET crew_role_id = ? WHERE account_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, crew_role_id);
		stmt.setString(2, account_id);
		int row = stmt.executeUpdate();
		if(row == 0)
			throw new UpdateFailure("Update failured");
	} 

	public void deleteCrewMember(Connection con, CrewData crewData) throws SQLException, NotFoundException, DeleteFailure{
		String account_id = getAccountId(con, crewData);
		String query = "DELETE FROM user_role WHERE account_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);
		int row = stmt.executeUpdate();
		if(row == 0)
			throw new DeleteFailure("Delete failured");
	} 

	// public void insertCrewMember(Connection con, UserAccount userAccount) throws SQLException {
	// 	String query = "INSERT INTO crew (name) VALUES (?)";

	// 	PreparedStatement stmt = con.prepareStatement(query);
	// 	stmt.setString(1, userAccount.getN());

	// 	int row = stmt.executeUpdate();
	// 	if (row == 0)
	// 		throw new SQLException("A permission is failed when adding!");

	// 	System.out.println("Add a permission successfully!");
	// }


}
