package dto.app;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

// TODO: REFACTOR
public class GetEventDTO {
	@Setter
	@Getter
	Integer id;

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
