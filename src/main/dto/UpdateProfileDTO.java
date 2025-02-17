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
	private String email;

	private String dateOfBirth;

	private String sex;

	public boolean hasNullFields() {
		return sex == null || fullName == null || contactEmail == null || email == null || dateOfBirth == null;
	}

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
}
