package dto;

import constants.Sex;
import lombok.Getter;

import java.sql.Date;

public class UpdateProfileDTO {

	@Getter
	private String fullName;

	@Getter
	private String contactEmail;

	@Getter
	private String studentCode;

	@Getter
	private String email;

	private String dateOfBirth;

	private String sex;

	public Date getDateOfBirth() {
		try {
			return Date.valueOf(dateOfBirth); // Convert "yyyy-MM-dd" to java.sql.Date
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public Sex getSex() {
		try {
			return Sex.valueOf(this.sex.toUpperCase()); // Convert "yyyy-MM-dd" to java.sql.Date
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public void checkNullOrEmpty() throws IllegalArgumentException {
		if (fullName == null || fullName.trim().isEmpty()) {
			throw new IllegalArgumentException("Full name cannot be null or empty");
		}
		if (contactEmail == null || contactEmail.trim().isEmpty()) {
			throw new IllegalArgumentException("Contact email cannot be null or empty");
		}
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be null or empty");
		}
		if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
			throw new IllegalArgumentException("Date of birth cannot be null or empty");
		}
		if (sex == null || sex.trim().isEmpty()) {
			throw new IllegalArgumentException("Sex cannot be null or empty");
		}
	}
}
