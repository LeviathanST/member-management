package dto.crew;

import lombok.Getter;

public class UpdateCrewDTO {
	@Getter
	String newCrewName;

	public void checkNullOrEmpty() throws IllegalArgumentException {
		if (this.newCrewName == null || this.newCrewName.isEmpty()) {
			throw new IllegalArgumentException("New crew name is empty or null");
		}
	}
}
