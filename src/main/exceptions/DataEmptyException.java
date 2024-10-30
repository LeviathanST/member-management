package exceptions;

public class DataEmptyException extends Exception {
	public String message;

	public DataEmptyException(String message) {
		super(message);
	}
};
