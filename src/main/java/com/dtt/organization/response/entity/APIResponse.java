package com.dtt.organization.response.entity;

import java.io.Serializable;

public class APIResponse implements Serializable {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The status.
	 */
	private boolean success;

	/**
	 * The message.
	 */
	private String message;

	/**
	 * The result.
	 */
	private String result;


	public APIResponse(boolean success, String message, String result) {
		this.success = success;
		this.message = message;
		this.result = result;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */


	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "APIResponse [success=" + success + ", message=" + message + ", result=" + result + "]";
	}
}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	
	
