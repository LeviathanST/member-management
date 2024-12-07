package models;

public class CrewRole {
	private String name;
	private int crewId;
	private int id;
	private String crewName;

	public CrewRole(int crewId, String name) {
		this.crewId = crewId;
		this.name = name;
	}

	public CrewRole(String role, String crewName) {
		this.name = role;
		this.crewName = crewName;
	}

	public String getName() {
		return name;
	}

	public int getCrewId() {
		return crewId;
	}

	public void setCrewId(int crewId) {
		this.crewId = crewId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCrewName() {
		return crewName;
	}
}
