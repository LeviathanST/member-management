package models;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

public class GuildEvent {
	@Setter
	@Getter
	private int id;
	@Setter
	@Getter
	private int guildId;
	@Setter
	@Getter
	private String title;
	@Setter
	@Getter
	private String description;
	@Setter
	@Getter
	private Timestamp startedAt;
	@Setter
	@Getter
	private Timestamp endedAt;

	public GuildEvent(String guildName, int id, String title, String description,
			Timestamp startedAt,
			Timestamp endedAt) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
	}
}
