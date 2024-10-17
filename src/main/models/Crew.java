package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import data.CrewData;
import exceptions.NotFoundException;
import java.sql.ResultSet;

public class Crew {
    private String name;

    public Crew() {}

    public Crew(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public static int getCrewId(Connection con, CrewData crewData) throws SQLException, NotFoundException {
		String query = "SELECT * FROM crew WHERE name = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, crewData.getName());
		ResultSet rs = stmt.executeQuery();

		if (!rs.next()) {
			throw new NotFoundException("Crew ID is not existed!");
		}
		return rs.getInt("id");
	}
}
