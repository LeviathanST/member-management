package exceptions;

public class TokenPairException extends Exception {
	public TokenPairException(String message) {
		super(message);
	}

	public TokenPairException(String message, Throwable cause) {
		super(message, cause);
	}
}
