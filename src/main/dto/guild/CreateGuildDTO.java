package dto.guild;

import lombok.Getter;

public class CreateGuildDTO {
	@Getter
	private String guildName;
	@Getter
	private String guildCode;
	@Getter
	private String username;

	public void checkNullOrEmpty() throws IllegalArgumentException {
		if (this.guildName == null || this.guildName.trim().isEmpty() || this.guildCode == null
				|| this.guildCode.trim().isEmpty()
				|| this.username == null || this.username.trim().isEmpty()) {
			throw new IllegalArgumentException("Data is empty or null, please check again!");
		}
	}
}
