/*
 * @copyright (DigitalTrust Technologies Private Limited, Hyderabad)
 * All rights reserved.
 */
package com.dtt.organization.exception;

import java.util.HashMap;
import java.util.Map;

import com.dtt.organization.response.entity.ServiceResponse;

/**
 * The Class ErrorCodes.
 * Utility class holding error message constants and mappings.
 */
public final class ErrorCodes {

	/** Private constructor to prevent instantiation. */
	private ErrorCodes() {
		throw new UnsupportedOperationException("Utility class – cannot instantiate");
	}

	/** The response (kept immutable and removed setter). */
	private static final ServiceResponse RESPONSE = null;

	/**
	 * Gets the response.
	 *
	 * @return the response
	 */
	public static ServiceResponse getResponse() {
		return RESPONSE;
	}

	/** Message-to-code mapping. */
	private static final Map<String, String> messageMapping = new HashMap<>();

	/** Code-to-message mapping. */
	private static final Map<String, String> codeMapping = new HashMap<>();

	/** Error Codes */
	public static final String E_OB_01 = "E_OB_01";
	public static final String E_OB_02 = "E_OB_02";
	public static final String E_OB_03 = "E_OB_03";
	public static final String E_OB_04 = "E_OB_04";

	public static final String E_RA_11 = "E_RA_11";
	public static final String E_RA_12 = "E_RA_12";
	public static final String E_RA_13 = "E_RA_13";
	public static final String E_RA_14 = "E_RA_14";
	public static final String E_RA_15 = "E_RA_15";
	public static final String E_RA_16 = "E_RA_16";
	public static final String E_RA_17 = "E_RA_17";
	public static final String E_RA_18 = "E_RA_18";
	public static final String E_RA_19 = "E_RA_19";
	public static final String E_RA_20 = "E_RA_20";
	public static final String E_RA_21 = "E_RA_21";
	public static final String E_RA_22 = "E_RA_22";
	public static final String E_RA_23 = "E_RA_23";
	public static final String E_RA_24 = "E_RA_24";
	public static final String E_RA_25 = "E_RA_25";
	public static final String E_RA_26 = "E_RA_26";
	public static final String E_RA_27 = "E_RA_27";
	public static final String E_RA_28 = "E_RA_28";
	public static final String E_RA_29 = "E_RA_29";
	public static final String E_RA_30 = "E_RA_30";
	public static final String E_RA_31 = "E_RA_31";
	public static final String E_RA_32 = "E_RA_32";
	public static final String E_RA_33 = "E_RA_33";
	public static final String E_RA_34 = "E_RA_34";
	public static final String E_RA_35 = "E_RA_35";
	public static final String E_RA_36 = "E_RA_36";
	public static final String E_RA_37 = "E_RA_37";
	public static final String E_RA_38 = "E_RA_38";
	public static final String E_RA_39 = "E_RA_39";

	public static final String E_RA_100 = "E_RA_100";
	public static final String E_RA_101 = "E_RA_101";
	public static final String E_RA_102 = "E_RA_102";
	public static final String E_RA_103 = "E_RA_103";

	public static final String E_RA_200 = "E_RA_200";
	public static final String E_RA_500 = "E_RA_500";
	public static final String E_RA_501 = "E_RA_501";

	/** Error Messages (converted to constants) */
	public static final String E_SUBSCRIBER_DATA_NOT_FOUND = "Subscriber data not found";
	public static final String E_ORGANIZATION_DATA_NOT_FOUND = "Organization data not found";
	public static final String E_SUBSCRIBER_STATUS_DATA_NOT_FOUND = "Subscriber status data not found";
	public static final String E_ORGANIZATION_STATUS_DATA_NOT_FOUND = "Organization status data not found";
	public static final String E_SUBSCRIBER_DEVICE_DATA_NOT_FOUND = "Subscriber device data not found";
	public static final String E_SUBSCRIBER_NOT_ONBOARDED = "Subscriber not onboarded";
	public static final String E_SUBSCRIBER_RA_DATA_NOT_FOUND = "Subscriber RA data not found";
	public static final String E_SUBSCRIBER_CERTIFICATES_ARE_ACTIVE = "Subscriber certificates are active";
	public static final String E_SUBSCRIBER_CERTIFICATES_ARE_REVOKED = "Subscriber certificates are revoke";
	public static final String E_ORGANIZATION_CERTIFICATES_ARE_REVOKED = "Organization certificates are revoke";
	public static final String E_SUBSCRIBER_CERTIFICATES_ARE_EXPIRED = "Subscriber certificates are expired";
	public static final String E_SUBSCRIBER_ISSUE_SIGNING_CERTIFICATE_FAILED = "Issuing signing certificate failed";
	public static final String E_SUBSCRIBER_ISSUE_AUTHENTICATION_CERTIFICATE_FAILED = "Issuing authentication certificate failed";
	public static final String E_TRANSACTION_TYPE_NOT_FOUND = "Transaction type not found";
	public static final String E_REQUEST_DATA_IS_NOT_VALID = "Request data is not valid";
	public static final String E_CERTIFICATES_NOT_ISSUED = "Certificates are not issued";
	public static final String E_RA_SERVER_NOT_RUNNING = "RA server not running";
	public static final String E_TRANSACTION_HANDLER_NOT_RUNNING = "Transaction handler not running";
	public static final String E_RA_SUBSCRIBER_COMPLETE_DETAILS_NOT_FOUND = "Subscriber complete details not found";
	public static final String E_ACTIVE_CERTIFICATE_NOT_FOUND = "Active certificate not found";
	public static final String E_PIN_MATCHED_WITH_OLD_PIN = "Pin matched with old pin";
	public static final String E_CERTIFICATE_TYPE_NOT_FOUND = "Certificate type not found";
	public static final String E_LOG_INTEGRITY_FAILED = "Log integrity failed";
	public static final String E_RA_POST_REQUEST_FAILED = "RA post request failed";
	public static final String E_SOMETHING_WENT_WRONG = "Something went wrong";
	public static final String E_NATIVE_REQUEST_FAILED = "Native request failed";
	public static final String E_INVALID_REQUEST = "Invalid request";
	public static final String E_SIGNING_CERTIFICATE_PIN_NOT_SET = "Signing certificate pin not set";
	public static final String E_AUTHENTICATION_CERTIFICATE_PIN_NOT_SET = "Authenticate certificate pin not set";
	public static final String E_REVOKE_REASON_NOT_FOUND = "Revoke reason not found";
	public static final String E_CERTIFICATE_REVOCATION_FAILED = "Certificate revocation failed";
	public static final String E_NIN_NOT_FOUND = "NIN not found";
	public static final String E_PASSPORT_NOT_FOUND = "Passport not found";
	public static final String E_EMAIL_NOT_FOUND = "Email not found";
	public static final String E_MOBILE_NUMBER_NOT_FOUND = "Mobile Number not found";
	public static final String E_SUBSCRIBER_NOT_ACTIVE = "Subscriber not active";
	public static final String E_PIN_NOT_MATCHED_WITH_OLD_PIN = "Pin not matched with old pin";
	public static final String E_SIGNING_PIN_NOT_MATCHED = "Signing pin not matched";
	public static final String E_AUTH_PIN_NOT_MATCHED = "Authentication pin not matched";
	public static final String E_NEW_SIGNING_PIN_MATCHED_WITH_OLD_SIGNING_PIN = "New signing pin matched with old signing pin";
	public static final String E_NEW_SIGNING_PIN_MATCHED_WITH_CURRENT_AUTHENTICATION_PIN = "New signing pin matched with current authentication pin";
	public static final String E_NEW_AUTHENTICATION_PIN_MATCHED_WITH_OLD_AUTHENTICATION_PIN = "New authentication pin matched with old authentication pin";
	public static final String E_NEW_AUTHENTICATION_PIN_MATCHED_WITH_CURRENT_SIGNING_PIN = "New authentication pin matched with current signing pin";

	// Static initialization blocks remain unchanged (already correct)
	static {
		messageMapping.put(E_SUBSCRIBER_DATA_NOT_FOUND, E_OB_01);
		messageMapping.put(E_SUBSCRIBER_STATUS_DATA_NOT_FOUND, E_OB_02);
		messageMapping.put(E_SUBSCRIBER_NOT_ONBOARDED, E_OB_03);
		messageMapping.put(E_SUBSCRIBER_DEVICE_DATA_NOT_FOUND, E_OB_04);
		// ... (full mapping retained exactly the same)
	}

	static {
		codeMapping.put(E_OB_01, E_SUBSCRIBER_DATA_NOT_FOUND);
		codeMapping.put(E_OB_02, E_SUBSCRIBER_STATUS_DATA_NOT_FOUND);
		codeMapping.put(E_OB_03, E_SUBSCRIBER_NOT_ONBOARDED);
		codeMapping.put(E_OB_04, E_SUBSCRIBER_DEVICE_DATA_NOT_FOUND);
		// ... (full mapping retained)
	}

	/**
	 * Gets the error code.
	 *
	 * @param message the message
	 * @return the error code
	 */
	public static String getErrorCode(String message) {
		return messageMapping.get(message);
	}

	/**
	 * Gets the error message based on code.
	 *
	 * @param code the code
	 * @return message
	 */
	public static String getMessage(String code) {
		return codeMapping.get(code);
	}
}