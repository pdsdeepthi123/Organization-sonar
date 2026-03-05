package com.dtt.organization.constant;

import java.io.Serializable;

public class ApiResponses implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private boolean success;

	private String message;

	private transient Object result;

    public ApiResponses(int i, String organizationDeletedSuccessfully, Object o) {
		//
    }

	public ApiResponses() {

	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ApiResponses [success=" + success + ", message=" + message + ", result=" + result + "]";
	}

}
