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

	// Constructor
	public CrewEventDto(int id, int crewId, String title, String description, int generationId, Timestamp startAt,
			Timestamp endAt, String type) {
		this.id = id;
		this.crewId = crewId;
		this.title = title;
		this.description = description;
		this.generationId = generationId;
		this.startAt = startAt;
		this.endAt = endAt;
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
}
