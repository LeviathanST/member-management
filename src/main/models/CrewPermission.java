package models;

public class CrewPermission {
	private int id;
	private String name;

	public CrewPermission(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public int getId() {
		return id;
	}
}
