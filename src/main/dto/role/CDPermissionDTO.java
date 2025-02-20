package dto.role;

import java.util.List;

import lombok.Getter;

/// NOTE:
/// Create and Delete permission for role
public class CDPermissionDTO {
	@Getter
	String roleName;

	@Getter
	List<String> permissions;
}
