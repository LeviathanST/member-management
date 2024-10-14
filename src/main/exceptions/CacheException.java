package exceptions;

public class CacheException extends Exception {
	public CacheException(String message) {
		super(message);
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}
}
