package dto.crew;

import lombok.Getter;

public class CreateCrewDTO {
	@Getter
	private String crewName;
	@Getter
	private String crewCode;
	@Getter
	private String username;

	public void checkNullOrEmpty() throws IllegalArgumentException {
		if (this.crewName == null || this.crewName.isEmpty() || this.crewCode == null || this.crewCode.isEmpty()
				|| this.username == null || this.username.isEmpty()) {
			throw new IllegalArgumentException("Data is empty or null, please check again!");
		}
	}
}
