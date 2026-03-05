/*
 * @copyright (DigitalTrust Technologies Private Limited, Hyderabad) 2021,
 * All rights reserved.
 */
package com.dtt.organization.dto;
/**
 * The Class RequestEntity.
 * @param <T> the type of the post request body
 */
public class RequestEntity<T> {

	/** The Constant serialVersionUID. */


	/** The post request payload of type T. */
	private T postRequest;

	/** The transaction type. */
	private String transactionType;

	/**
	 * Gets the post request.
	 *
	 * @return the post request of type T
	 */
	public T getPostRequest() {
		return postRequest;
	}

	/**
	 * Sets the post request.
	 *
	 * @param postRequest
	 * the new post request of type T
	 */
	public void setPostRequest(T postRequest) {
		this.postRequest = postRequest;
	}

	/**
	 * Gets the transaction type.
	 *
	 * @return the transaction type
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * Sets the transaction type.
	 *
	 * @param transactionType
	 * the new transaction type
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/*
	 * (non-Javadoc)
	 * * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RequestEntity [postRequest=" + postRequest + ", transactionType=" + transactionType + "]";
	}

}