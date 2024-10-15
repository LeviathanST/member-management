package utils;

import java.util.HashMap;
import java.util.HashSet;

import constants.RoleType;
import exceptions.CacheException;

public class PermissionCache {
	/// Type: Application, Crew, Guild
	/// Name:
	/// + Application: President, Vice President
	/// + Guild: Media, HR, Technical, Plan
	/// + Crew: AI, BE, FE, SECURITY, GAME
	/// Name Permission:
	/// EX: Write, Edit, Delete, ...

	private static HashMap<RoleType, HashMap<String, HashSet<String>>> permissions = new HashMap<>();

	public static void addPermission(RoleType type, String name, String namePermission) throws CacheException {
		permissions.putIfAbsent(type, new HashMap<>());

		permissions.get(type).putIfAbsent(name, new HashSet<String>());

		boolean isAdded = permissions.get(type).get(name).add(namePermission);

		if (!isAdded) {
			throw new CacheException("This permission is already contained in cache!");
		}
	}

	public static HashSet<String> contains(RoleType type, String name, String namePermission) {
		if (permissions.containsKey(type) && permissions.get(type).containsKey(name)) {
			return permissions.get(type).get(name);
		}
		return null;
	}

	public void clear() {
		permissions.clear();
	}
}
