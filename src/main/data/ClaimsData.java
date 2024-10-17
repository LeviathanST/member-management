package data;

import java.util.List;

public class ClaimsData {
	private String username;
	private int userRoleId;
	private List<Integer> userGuildRoleIds;
	private List<Integer> userCrewRoleIds;

	public ClaimsData(String username, int userRoleId,
			List<Integer> userGuildRoleIds,
			List<Integer> userCrewRoleIds) {
		this.username = username;
		this.userRoleId = userRoleId;
		this.userGuildRoleIds = userGuildRoleIds;
		this.userCrewRoleIds = userCrewRoleIds;
	}

	public String getUsername() {
		return this.username;
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
