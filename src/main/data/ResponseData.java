package data;

import constants.ResponseStatus;

public class ResponseData<T> {
	private ResponseStatus status;
	private String message;
	private T data;

	public ResponseData(ResponseStatus status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public ResponseStatus getStatus() {
		return this.status;
	}

	public String getMessage() {
		return this.message;
	}

	public T getData() {
		return this.data;
	}
}
