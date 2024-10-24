package dto;

import constants.CrewEventType;
import constants.GuildEventType;

import java.sql.Timestamp;

public class GuildEventDTO {
	private int guildId;
	private String title;
	private String description;
	private int generationId;
	private Timestamp startAt;
	private Timestamp endAt;
	private GuildEventType type;

	// Getter and Setter for crewId
	public int getGuildId() {
		return guildId;
	}

	public void setGuildId(int guildId) {
		this.guildId = guildId;
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
	public GuildEventType getType() {
		return type;
	}

	public void setType(GuildEventType type) {
		this.type = type;
	}
}
