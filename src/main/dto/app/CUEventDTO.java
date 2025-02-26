package dto.app;

import lombok.Getter;

// TODO: REFACTOR
public class CUEventDTO {
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
