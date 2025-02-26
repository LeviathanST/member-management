package dto.guild;

import lombok.Getter;

public class UpdateGuildDTO {
	@Getter
	String newGuildName;

	public void checkNullOrEmpty() throws IllegalArgumentException {
		if (this.newGuildName == null || this.newGuildName.isEmpty()) {
			throw new IllegalArgumentException("New guild name is empty or null!");
		}
	}
}
