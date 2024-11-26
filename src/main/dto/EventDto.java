package dto;

import java.sql.Timestamp;

public class EventDto {
	private int id;
	private String title;
	private String description;
	private int generationId;
	private Timestamp startAt;
	private Timestamp endAt;
	private String type;
	private String generation;

	// Constructor
	public EventDto(String title, String description, int generationId, Timestamp startAt,
                    Timestamp endAt, String type) {
		this.title = title;
		this.description = description;
		this.generationId = generationId;
		this.startAt = startAt;
		this.endAt = endAt;
		this.type = type;
	}
	public EventDto(int id, String title, String description, String  generation, Timestamp startAt,
                    Timestamp endAt, String type) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.generation = generation;
		this.startAt = startAt;
		this.endAt = endAt;
		this.type = type;
	}
	public EventDto(int generationId, String title, String description, String type) {
		this.generationId = generationId;
		this.title = title;
		this.description = description;
		this.type = type;
	}


	public int getId() {
		return id;
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

	public String getGeneration() {return generation;}
	public void setGeneration(String generation) {this.generation = generation;}
}
