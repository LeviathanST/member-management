package models.roles;

import java.util.List;
import models.permissions.Permission;

public class Role {
	private String id;
	private String name;
	private List<Permission> permissions;

	// TODO:
	// public static GuildRole getByAccountId(Connection con, String account_id) {
	//
	// }
}
