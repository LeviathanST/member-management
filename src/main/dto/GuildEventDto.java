package dto;

import java.sql.Timestamp;

public class GuildEventDto {
	private int id;
	private int guildId;
	private String title;
	private String description;
	private int generationId;
	private Timestamp startAt;
	private Timestamp endAt;
	private String type;

	// Constructor
	public GuildEventDto(int id, int guildId, String title, String description, int generationId, Timestamp startAt,
			Timestamp endAt, String type) {
		this.id = id;
		this.guildId = guildId;
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

	public int getGuildId() {
		return guildId;
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
