package dto.crew;

import lombok.Getter;

public class CUCrewEventDTO {
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
