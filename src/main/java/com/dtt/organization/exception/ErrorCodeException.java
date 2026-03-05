package com.dtt.organization.exception;

import java.util.HashMap;
import java.util.Map;


public enum ErrorCodeException {

	// General
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

	private final String code;
	private final String messageKey;

	ErrorCodeException(String code, String messageKey) {
		this.code = code;
		this.messageKey = messageKey;
	}

	public String getCode() {
		return code;
	}

	public String getMessageKey() {
		return messageKey;
	}

	protected static final Map<Integer, String> map = new HashMap<>();

	static {
		map.put(400, BAD_REQUEST.getCode());
		map.put(401, UNAUTHORIZED.getCode());
		map.put(403, FORBIDDEN.getCode());
		map.put(404, NOT_FOUND.getCode());
		map.put(500, INTERNAL_SERVER_ERROR.getCode());
		map.put(503, SERVICE_UNAVAILABLE.getCode());
		map.put(408, REST_CONNECTION_ERROR.getCode());
	}

	public static ErrorCodeException fromCode(String code) {
		for (ErrorCodeException e : values()) {
			if (e.code.equals(code)) {
				return e;
			}
		}
		return UNKNOWN_ERROR;
	}
}


