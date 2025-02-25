package dto.guild;

import lombok.Getter;

public class CUGuildEventDTO {
	@Getter
	Integer eventId;

	@Getter
	String title;

	@Getter
	String description;

	@Getter
	String startedAt;

	@Getter
	String endedAt;
}
