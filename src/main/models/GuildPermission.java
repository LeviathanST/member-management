package models;

public class GuildPermission {
	private int id;
	private String name;

	public GuildPermission(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}
}
