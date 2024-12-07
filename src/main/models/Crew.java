package models;

public class Crew {
	private int id;
	private String name;

	public Crew(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Crew(String name) {
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
}
