package dto.guild;

import lombok.Getter;

public class AddUserToGuildDTO {
	@Getter
	String guildName;
	@Getter
	String guildCode;
	@Getter
	String username;

	public boolean checkNullAndEmpty() {
		return this.guildName != null && this.guildName.trim().isEmpty() &&
				this.guildCode != null && this.guildCode.trim().isEmpty() &&
				this.username != null && this.username.trim().isEmpty();
	}
}
