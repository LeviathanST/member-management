package exceptions;

public class DataEmptyException extends Exception {
	public String message;

	public DataEmptyException(String message) {
		this.message = message;
		System.err.println("Exception Data Empty: " + message);
		printStackTrace();
	}
};
