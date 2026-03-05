package com.dtt.organization.asserts;

import com.dtt.organization.exception.OrgnizationServiceException;

public class OrgnizationServiceAsserts {

	private OrgnizationServiceAsserts() {
		throw new UnsupportedOperationException("Utility class - cannot be instantiated");
	}



	/**
	 * Not nullor empty.
	 *
	 * @param orgumentName
	 *            the orgument name
	 * @param orgumentName1
	 *            the orgument name 1
	 * @throws RAServiceException
	 *             the RA service exception
	 */
	public static void notNullorEmpty(String orgumentName, String orgumentName1) throws OrgnizationServiceException {
		if (orgumentName == null || orgumentName.isEmpty()) {
			throw new OrgnizationServiceException(orgumentName1);
		}
	}

	/**
	 * Not nullor empty.
	 *
	 * @param size
	 *            the size
	 * @param orgumentName
	 *            the orgument name
	 * @throws RAServiceException
	 *             the RA service exception
	 */
	public static void notNullorEmpty(int size, String orgumentName) throws OrgnizationServiceException {
		if (size == 0) {
			throw new OrgnizationServiceException(orgumentName);
		}
	}

	/**
	 * Not nullor empty.
	 *
	 * @param orgumentName
	 *            the orgument name
	 * @param message
	 *            the message
	 * @throws RAServiceException
	 *             the RA service exception
	 */
	public static void notNullorEmpty(Object orgumentName, String message) throws OrgnizationServiceException {
		if (orgumentName == null) {
			throw new OrgnizationServiceException(message);
		}
	}
}
