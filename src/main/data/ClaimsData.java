package data;

import java.util.List;

public class ClaimsData {
	private String accountId;
	private int userRoleId;
	private List<Integer> userGuildRoleIds;
	private List<Integer> userCrewRoleIds;

	public ClaimsData(String accountId, int userRoleId,
			List<Integer> userGuildRoleIds,
			List<Integer> userCrewRoleIds) {
		this.accountId = accountId;
		this.userRoleId = userRoleId;
		this.userGuildRoleIds = userGuildRoleIds;
		this.userCrewRoleIds = userCrewRoleIds;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public int getUserRoleId() {
		return this.userRoleId;
	}

	public List<Integer> getUserGuildRoleId() {
		return this.userGuildRoleIds;
	}

	public List<Integer> getUserCrewRoleId() {
		return this.userCrewRoleIds;
	}
}
