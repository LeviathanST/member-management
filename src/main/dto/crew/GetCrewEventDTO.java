
package dto.crew;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

public class GetCrewEventDTO {
	@Setter
	@Getter
	Integer id;

	@Setter
	@Getter
	String crewName;

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
