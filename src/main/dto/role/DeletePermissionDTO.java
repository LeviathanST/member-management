package dto.role;

import java.util.List;

import lombok.Getter;

public class DeletePermissionDTO {
	@Getter
	String roleName;

	@Getter
	List<String> permissions;
}
