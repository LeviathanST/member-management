package utils;

public class Pressessor {
	public static String removePrefixFromRole(String role) {
		int index = role.indexOf('_');
		if (index != -1) {
			return role.substring(index + 1);
		}
		return role;
	}
}
