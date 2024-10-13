package data;

import com.googlecode.lanterna.terminal.swing.TerminalScrollController.Null;

public class SignUpData {
	private String username;
	private String password;

	public SignUpData(String username, String password) {
		this.username = username;
		String[] errors = validatePassword(password);
		if(errors.length == 0)
			this.password = password;
		else this.password = null;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pwd) {
		String[] errors = validatePassword(pwd);
		if(errors.length == 0)
			this.password = pwd;
		else this.password = null;
	}

	public static String[] validatePassword(String password) {
        // List to hold validation error messages
        java.util.List<String> errors = new java.util.ArrayList<>();

        if (password == null) {
            errors.add("Password cannot be null.");
            return errors.toArray(new String[0]);
        }

        if (password.length() <= 8) {
            errors.add("Password must be longer than 8 characters.");
        }

        if (!password.matches(".*[A-Z].*")) {
            errors.add("Password must contain at least one uppercase letter.");
        }

        if (!password.matches(".*[a-z].*")) {
            errors.add("Password must contain at least one lowercase letter.");
        }

        if (!password.matches(".*\\d.*")) {
            errors.add("Password must contain at least one digit.");
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            errors.add("Password must contain at least one special character (@#$%^&+=!).");
        }

        return errors.toArray(new String[0]);
    }
}
