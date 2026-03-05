package com.dtt.organization.exception;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {

	GENERIC_ERROR("E0001", "api.error.generic"),
	CONNECTION_ERROR("E0002", "api.error.connection"),
	DATABASE_ERROR("E0003", "api.error.database"),
	VALIDATION_ERROR("E0004", "api.error.validation"),

	// HTTP
	BAD_REQUEST("E400", "api.error.bad.request"),
	UNAUTHORIZED("E401", "api.error.unauthorized"),
	FORBIDDEN("E403", "api.error.forbidden"),
	NOT_FOUND("E404", "api.error.not.found"),
	INTERNAL_SERVER_ERROR("E500", "api.error.internal"),
	SERVICE_UNAVAILABLE("E503", "api.error.service.unavailable"),

	REST_CONNECTION_ERROR("E1001", "api.error.rest.connection"),
	REST_CLIENT_ERROR("E1002", "api.error.rest.client"),
	REST_SERVER_ERROR("E1003", "api.error.rest.server"),
	UNKNOWN_ERROR("E9999", "api.error.unknown"),

	// PKI
	E_ORGANIZATION_CERTIFICATES_ARE_REVOKED("E005", "api.error.pki.cert.revoked"),
	E_REVOKE_REASON_NOT_FOUND("E006", "api.error.pki.revoke.reason.not.found"),
	E_ORGANIZATION_DATA_NOT_FOUND("E007", "api.error.pki.org.data.not.found"),
	E_ORGANIZATION_STATUS_DATA_NOT_FOUND("E008", "api.error.pki.org.status.not.found"),
	E_REQUEST_DATA_IS_NOT_VALID("E009", "api.error.pki.request.invalid"),
	E_TRANSACTION_TYPE_NOT_FOUND("E0010", "api.error.pki.transaction.not.found"),
	E_WRAPPED_KEY_NOT_FOUND("E0011", "api.error.pki.wrapped.key.not.found"),
	E_CERTIFICATE_TYPE_NOT_FOUND("E0012", "api.error.pki.cert.type.not.found"),
	E_ACTIVE_CERTIFICATE_NOT_FOUND("E0013", "api.error.pki.active.cert.not.found");


	// Map for HTTP status codes to error codes
    private static final Map<Integer, String> map = new HashMap<>();

    static {
        // Map common HTTP status codes to specific error codes
    	map.put(400, BAD_REQUEST.getCode());  // BAD_REQUEST
        map.put(401, UNAUTHORIZED.getCode());  // UNAUTHORIZED
        map.put(403, FORBIDDEN.getCode());  // FORBIDDEN
        map.put(404, NOT_FOUND.getCode());  // NOT_FOUND
        map.put(500, INTERNAL_SERVER_ERROR.getCode());  // INTERNAL_SERVER_ERROR
        map.put(503, SERVICE_UNAVAILABLE.getCode());  // SERVICE_UNAVAILABLE
        map.put(408, REST_CONNECTION_ERROR.getCode());  // REQUEST_TIMEOUT
    }


	private final String code;
	private final String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public static String getMessageByCode(String code) {
		for (ErrorCode errorCode : ErrorCode.values()) {
			if (errorCode.getCode().equals(code)) {
				return errorCode.getMessage();
			}
		}
		return "Unknown error code"; // Default message if the code is not found
	}
}
