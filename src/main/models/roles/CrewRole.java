package models.roles;

import java.sql.Connection;

public class CrewRole {
	private int crew_id;
	private String name;
	private Connection con;

	public CrewRole(int crew_id, String name, Connection con) {
		this.crew_id = crew_id;
		this.name = name;
		this.con = con;
	}

	public int getCrewId() {
		return this.crew_id;
	}

	public String getName() {
		return this.name;
	}

}
