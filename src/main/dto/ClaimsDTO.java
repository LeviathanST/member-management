package dto;

import java.util.List;

import lombok.Getter;

public class ClaimsDTO {
	@Getter
	private String accountId;
	@Getter
	private int userRoleId;

	public ClaimsDTO(String accountId, int userRoleId) {
		this.accountId = accountId;
		this.userRoleId = userRoleId;
	}
}
