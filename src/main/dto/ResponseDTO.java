package dto;

import constants.ResponseStatus;

public class ResponseDTO<T> {
	private ResponseStatus status;
	private String message;
	private T data;

	public ResponseDTO(ResponseStatus status, String message, T data) {
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
