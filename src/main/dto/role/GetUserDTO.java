package dto.role;

import lombok.Getter;
import lombok.Setter;

public class GetUserDTO {
	@Getter
	@Setter
	String username;

	@Getter
	@Setter
	String fullName;

	@Getter
	@Setter
	String role;
}
