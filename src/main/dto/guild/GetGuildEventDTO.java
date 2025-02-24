package dto.guild;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

public class GetGuildEventDTO {
	@Setter
	@Getter
	int id;

	@Setter
	@Getter
	String guildName;

	@Setter
	@Getter
	String title;

	@Setter
	@Getter
	String description;

	@Setter
	@Getter
	Timestamp startedAt;

	@Setter
	@Getter
	Timestamp endedAt;
}
