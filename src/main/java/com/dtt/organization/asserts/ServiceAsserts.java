package com.dtt.organization.asserts;

import com.dtt.organization.exception.RAServiceException;

public class ServiceAsserts {

	private ServiceAsserts() {
		throw new UnsupportedOperationException("Utility class - cannot be instantiated");
	}


	public static void notNullorEmpty(String orgumentName, String orgumentName1) throws RAServiceException {
		if (orgumentName == null || orgumentName.isEmpty()) {
			throw new RAServiceException(orgumentName1);
		}
	}


	public static void notNullorEmpty(int size, String orgumentName) throws RAServiceException {
		if (size == 0) {
			throw new RAServiceException(orgumentName);
		}
	}


	public static void notNullorEmpty(Object orgumentName, String message) throws RAServiceException {
		if (orgumentName == null) {
			throw new RAServiceException(message);
		}
	}
}
