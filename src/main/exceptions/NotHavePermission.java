package exceptions;

public class NotHavePermission extends Exception {
	public NotHavePermission(String message) {
		super(message);
	}

	public NotHavePermission(String message, Throwable cause) {
		super(message, cause);
	}
}
