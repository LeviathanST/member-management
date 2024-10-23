package data;

import constants.ResponseStatus;

public class ResponseData {
	private ResponseStatus status;
	private String message;
	private Object data;

	public ResponseData(ResponseStatus status, String message, Object data) {
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

	public Object getData() {
		return this.data;
	}
}
