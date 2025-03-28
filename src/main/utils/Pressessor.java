package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

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

	public static String isValidEmail(String email) throws IllegalArgumentException {
		String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		if (!email.matches(pattern)) {
			throw new IllegalArgumentException("Invalid email format");
		}
		return email;
	}

	public static boolean validateStudentCode(String studentCode) {
		List<String> VALID_MAJORS = Arrays.asList(
				"SE", // Software Engineering
				"AI", // Artificial Intelligence
				"IA", // Information Assurance
				"IS", // Information Systems
				"IT", // IoT (Internet of Things)
				"IC", // Integrated Circuit
				"AS", // Automotive Software Engineering
				"DA", // Digital Art Design
				"IB", // International Business
				"BA", // Business Administration
				"MM", // Multimedia
				"DM", // Digital Marketing
				"TT", // Tourism & Travel Service Management
				"HM", // Hotel Management
				"ES", // English Studies
				"JL", // Japanese Language
				"KL" // Korean Language
		);
		if (studentCode == null || studentCode.trim().isEmpty()) {
			throw new IllegalArgumentException("Student code cannot be null or empty");
		}

		if (studentCode.length() != 8) {
			throw new IllegalArgumentException("You student code format is wrong!");
		}

		String major = studentCode.substring(0, 2); // First 2 characters
		String number = studentCode.substring(2); // Last 6 characters

		if (!VALID_MAJORS.contains(major)) {
			throw new IllegalArgumentException("Is there code like %s in our school?");
		}

		if (!number.matches("\\d{6}")) {
			throw new IllegalArgumentException("You student code format is wrong!");
		}

		return true;
	}

	public static String validateRoleName(String roleName) throws IllegalArgumentException {
		String pattern = "^[a-zA-Z0-9_-]+$";
		if (!roleName.matches(pattern)) {
			throw new IllegalArgumentException(
					"Please name the role in the following format! \nFor example: Admin, User-Group, Role_Name");
		}
		return roleName;
	}

	public static boolean validPermission(String permission) {
		String pattern = "^[a-zA-Z0-9]+(\\.[a-zA-z0-9]+)*$";
		if (!permission.matches(pattern)) {
			return false;
		}
		return true;
	}

	public static String validCode(String code) {
		String pattern = "^[A-Z0-9]+$";
		if (!code.matches(pattern)) {
			throw new IllegalArgumentException("Invalid code format!");
		}
		return code;
	}
}
