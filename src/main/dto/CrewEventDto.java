package dto;

import java.sql.Timestamp;

public class CrewEventDto {
	private int id;
	private int crewId;
	private String title;
	private String description;
	private int generationId;
	private Timestamp startAt;
	private Timestamp endAt;
	private String type;

	private String crewName;
	private String generation;

	// Constructor
	public CrewEventDto(int crewId, String title, String description, int generationId, Timestamp startAt,
                        Timestamp endAt, String type) {
		this.crewId = crewId;
		this.title = title;
		this.description = description;
		this.generationId = generationId;
		this.startAt = startAt;
		this.endAt = endAt;
		this.type = type;
	}
	public CrewEventDto(int id, String crewName, String title, String description, int generationId, Timestamp startAt,
                        Timestamp endAt, String type) {
		this.id = id;
		this.crewName = crewName;
		this.title = title;
		this.description = description;
		this.generationId = generationId;
		this.startAt = startAt;
		this.endAt = endAt;
		this.type = type;
	}
	public CrewEventDto(String crewName, String generation, String title, String description, String type) {
		this.crewName = crewName;
		this.generation = generation;
		this.title = title;
		this.description = description;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public int getCrewId() {
		return crewId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public int getGenerationId() {
		return generationId;
	}

	public Timestamp getStartAt() {
		return startAt;
	}

	public Timestamp getEndAt() {
		return endAt;
	}

	public String getType() {
		return type;
	}

	public String getCrewName() {return crewName;}
	public void setCrewName(String crewName) {this.crewName = crewName;}

	public String getGeneration() {return generation;}
	public void setGeneration(String generation) {this.generation = generation;}
}
