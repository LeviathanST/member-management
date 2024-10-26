package dto;

import java.sql.Timestamp; // Correct import for Timestamp
import constants.CrewEventType;

public class CrewEventDTO {
	private int crewId;
	private String title;
	private String description;
	private int generationId;
	private Timestamp startAt;
	private Timestamp endAt;
	private CrewEventType type;

	// Getter and Setter for crewId
	public int getCrewId() {
		return crewId;
	}

	public void setCrewId(int crewId) {
		this.crewId = crewId;
	}

	// Getter and Setter for title
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// Getter and Setter for description
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// Getter and Setter for generationId
	public int getGenerationId() {
		return generationId;
	}

	public void setGenerationId(int generationId) {
		this.generationId = generationId;
	}

	// Getter and Setter for startAt
	public Timestamp getStartAt() {
		return startAt;
	}

	public void setStartAt(Timestamp startAt) {
		this.startAt = startAt;
	}

	// Getter and Setter for endAt
	public Timestamp getEndAt() {
		return endAt;
	}

	public void setEndAt(Timestamp endAt) {
		this.endAt = endAt;
	}

	// Getter and Setter for type
	public CrewEventType getType() {
		return type;
	}

	public void setType(CrewEventType type) {
		this.type = type;
	}
}
