package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pressessor {
	public static String removePrefixFromRole(String role) {
		int index = role.indexOf('_');
		if (index != -1) {
			return role.substring(index + 1);
		}
		return role;
	}

	public static void validateEventTime(LocalDateTime start, LocalDateTime end) throws IllegalArgumentException {
		LocalDateTime now = LocalDateTime.now();
		if (start.isBefore(now)) {
			throw new IllegalArgumentException("Start time cannot be in the past!");
		}
		if (end.isBefore(start)) {
			throw new IllegalArgumentException("End time must be after start time!");
		}
	}

	public static LocalDateTime parseDateTime(String dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(dateTime, formatter);
	}
}
