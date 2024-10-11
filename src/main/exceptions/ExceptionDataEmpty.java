package exceptions;

import java.lang.Exception;

public class ExceptionDataEmpty extends Exception {
	public String message;

	public ExceptionDataEmpty(String message) {
		this.message = message;
		System.err.println("Exception Data Empty: " + message);
		printStackTrace();
	}
};
