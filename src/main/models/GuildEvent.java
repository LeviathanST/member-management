package models;

import java.sql.Timestamp;

public class GuildEvent {
	private int id;
	private int guildId;
	private String title;
	private String description;
	private int generationId;
	private Timestamp startAt;
	private Timestamp endAt;
	private String type;

	private String guildName;;
	private String generation;

	// Constructor
	public GuildEvent(int guildId, String title, String description, int generationId, Timestamp startAt,
			Timestamp endAt, String type) {
		this.guildId = guildId;
		this.title = title;
		this.description = description;
		this.generationId = generationId;
		this.startAt = startAt;
		this.endAt = endAt;
		this.type = type;
	}

	public GuildEvent(int id, String guildName, String title, String description, int generationId,
			Timestamp startAt,
			Timestamp endAt, String type) {
		this.id = id;
		this.guildName = guildName;
		this.title = title;
		this.description = description;
		this.generationId = generationId;
		this.startAt = startAt;
		this.endAt = endAt;
		this.type = type;
	}

	public GuildEvent(String guildName, String generation, String title, String description, String type) {
		this.guildName = guildName;
		this.generation = generation;
		this.title = title;
		this.description = description;
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

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	public String getGeneration() {
		return generation;
	}

	public void setGeneration(String generation) {
		this.generation = generation;
	}
}
