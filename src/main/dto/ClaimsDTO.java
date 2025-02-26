package dto;

import lombok.Getter;

public class ClaimsDTO {
	@Getter
	private String accountId;
	@Getter
	private Integer userRoleId;

	public ClaimsDTO(String accountId, int userRoleId) {
		this.accountId = accountId;
		this.userRoleId = userRoleId;
	}
}
