package models;

import java.sql.Timestamp;
import java.util.UUID;

public class UserAccount {
	private String id;
	private String username;
	private String hashedPassword;
	private boolean isActive;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private int countMistake;

	// Constructor
	public UserAccount(String username, String hashedPassword) {
		this.id = UUID.randomUUID().toString(); // Automatically generates a unique ID
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.isActive = true; // Default to active
		this.createdAt = new Timestamp(System.currentTimeMillis());
		this.updatedAt = this.createdAt;
		this.countMistake = 0; // Default mistake count to 0
	}

	// Getter and Setter methods
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getCountMistake() {
		return countMistake;
	}

	public void setCountMistake(int countMistake) {
		this.countMistake = countMistake;
	}
}
