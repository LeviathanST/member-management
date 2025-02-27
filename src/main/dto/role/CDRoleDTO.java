package dto.role;

import lombok.Getter;

public class CDRoleDTO {
	@Getter
	String roleName;

	public void checkNullOrEmpty() throws IllegalArgumentException {
		if (this.roleName == null || this.roleName.trim().isEmpty()) {
			throw new IllegalArgumentException("Data is empty or null, please check again!");
		}
	}
}
