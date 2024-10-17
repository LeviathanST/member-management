package utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

import constants.RoleType;
import exceptions.CacheException;
import exceptions.NotFoundException;
import models.permissions.CrewPermission;
import models.permissions.GuildPermission;

public class PermissionCache {
	/// Type: Application, Crew, Guild
	/// Name:
	/// + Application: President, Vice President
	/// + Guild: Media, HR, Technical, Plan
	/// + Crew: AI, BE, FE, SECURITY, GAME
	/// Name Permission:
	/// EX: Write, Edit, Delete, ...

	private static HashMap<RoleType, HashMap<Integer, HashSet<String>>> permissions = new HashMap<>();

	/// @param role_id:
	/// + role_id
	/// + crew_role_id
	/// + guild_role_id
	public static void addPermission(Connection con, String accountId, RoleType type, int roleId)
			throws CacheException, NotFoundException, SQLException {
		boolean isAdded;
		int timeOut = 3;
		permissions.putIfAbsent(type, new HashMap<>());

		permissions.get(type).putIfAbsent(roleId, new HashSet<String>());

		try {
			switch (type) {
				case RoleType.Guild:
					List<GuildPermission> guildPermissions = GuildPermission
							.getAllByAccountIdAndRoleId(con,
									accountId, roleId);

					if (guildPermissions.isEmpty()) {
						throw new NotFoundException("This account is not have needed role!");
					}

					for (GuildPermission permission : guildPermissions) {
						isAdded = permissions.get(type).get(roleId).add(permission.getName());
						if (!isAdded) {
							return;
						}
					}
					break;
				case RoleType.Crew:
					List<CrewPermission> crewPermissions = CrewPermission.getAllByCrewRoleId(con,
							roleId);

					for (CrewPermission permission : crewPermissions) {
						isAdded = permissions.get(type).get(roleId).add(permission.getName());
						if (!isAdded) {
							return;
						}
					}
					break;
				case RoleType.Application:
					break;
				default:
					throw new NotFoundException("PermissionCache: Your role type is not found!");
			}
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		} catch (NotFoundException e) {
			throw new NotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new CacheException("This permission is already contained in cache!" + e.getMessage());
		}

	}

	// TODO:
	public static void load() {
	}

	public static boolean contains(RoleType type, int roleId, String namePermission) {
		if (permissions.containsKey(type) && permissions.get(type).containsKey(roleId)) {
			return permissions.get(type).get(roleId).contains(namePermission);
		}
		return false;
	}

	public static void clear(RoleType type, int roleId) {
		permissions.get(type).get(roleId).clear();
	}

	public static void clearAll() {
		permissions.clear();
	}
}
