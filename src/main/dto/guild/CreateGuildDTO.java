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
		if (this.guildName == null || this.guildName.isEmpty() || this.guildCode == null
				|| this.guildCode.isEmpty()
				|| this.username == null || this.username.isEmpty()) {
			throw new IllegalArgumentException("Data is empty or null, please check again!");
		}
	}
}
