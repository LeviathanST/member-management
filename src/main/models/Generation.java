package models;

public class Generation {
	private int id;
	private String name;

	public Generation(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Generation(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
