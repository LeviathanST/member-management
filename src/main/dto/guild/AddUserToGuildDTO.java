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
		return this.guildName != null && this.guildName.isEmpty() &&
				this.guildCode != null && this.guildCode.isEmpty() &&
				this.username != null && this.username.isEmpty();
	}
}
