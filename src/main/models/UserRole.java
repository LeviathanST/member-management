package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.management.relation.Role;

import exceptions.NotFoundException;
import models.roles.CrewRole;
import models.roles.GuildRole;

public class UserRole {
	private Role role;
	private CrewRole crew_role;
	private GuildRole guild_role;

	private Connection con;

	private UserRole(Role role, CrewRole crew_role, GuildRole guild_role, Connection con) {
		this.role = role;
		this.crew_role = crew_role;
		this.guild_role = guild_role;
		this.con = con;
	}

	// public static UserRole getByAccountId(Connection con, String account_id)
	// throws NotFoundException, SQLException {
	//
	// String query = "SELECT role_id, crew_role_id, guild_role_id FROM user_role
	// WHERE account_id = ?";
	//
	// PreparedStatement stmt = con.prepareStatement(query);
	// stmt.setString(1, account_id);
	// ResultSet rs = stmt.executeQuery();
	//
	// if (!rs.next()) {
	// throw new NotFoundException("AccountID is not existed!");
	// }
	//
	// return new UserRole();
	// }
}
