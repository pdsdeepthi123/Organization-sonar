package com.dtt.organization.exception;

import java.util.Locale;

import com.dtt.organization.constant.ApiResponses;
import org.hibernate.PessimisticLockException;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import com.dtt.organization.util.AppUtil;
import com.dtt.organization.util.Utility;

@Component
public class ExceptionHandlerUtil {

	private static final String CLASS = ExceptionHandlerUtil.class.getSimpleName();
	 static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerUtil.class);

	private final MessageSource messageSource;

	public ExceptionHandlerUtil(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public ApiResponses handleException(Exception e){

		Locale locale = LocaleContextHolder.getLocale();

		String errorKey = "api.error.generic";
		String errorCode = ErrorCodeException.GENERIC_ERROR.getCode();

		if (e instanceof JDBCConnectionException) {
			errorKey = "api.error.connection";
			errorCode = ErrorCodeException.CONNECTION_ERROR.getCode();

			logger.error("{} - {} : Database connection error occurred: {}",
					CLASS, Utility.getMethodName(), e.getMessage());

		} else if (e instanceof ConstraintViolationException
				|| e instanceof DataException
				|| e instanceof LockAcquisitionException
				|| e instanceof PessimisticLockException
				|| e instanceof QueryTimeoutException
				|| e instanceof SQLGrammarException
				|| e instanceof GenericJDBCException) {

			errorKey = "api.error.database";
			errorCode = ErrorCodeException.DATABASE_ERROR.getCode();

			logger.error("{} - {} : Database-related error occurred: {}",
					CLASS, Utility.getMethodName(), e.getMessage());

		} else {
			logger.error("{} - {} : An unexpected error occurred: {}",
					CLASS, Utility.getMethodName(), e.getMessage());
		}

		String errorMessage = messageSource.getMessage(errorKey, null, locale);
		String formattedMessage = String.format("%s [ErrorCode: %s]", errorMessage, errorCode);

		logger.info("{} - {} : Returning error response: errorCode={}, message={}",
				CLASS, Utility.getMethodName(), errorCode, formattedMessage);

		return createErrorResponse(formattedMessage);
	}


	public ApiResponses handleHttpException(Exception e) {

		Locale locale = LocaleContextHolder.getLocale();
		ErrorCodeException errorEnum;
		String formattedMessage;

		if (e instanceof HttpStatusCodeException httpEx) {

			HttpStatus status = HttpStatus.valueOf(httpEx.getStatusCode().value());

			switch (status) {
				case BAD_REQUEST -> errorEnum = ErrorCodeException.BAD_REQUEST;
				case UNAUTHORIZED, FORBIDDEN, NOT_FOUND -> errorEnum = ErrorCodeException.REST_CLIENT_ERROR;
				case INTERNAL_SERVER_ERROR -> errorEnum = ErrorCodeException.INTERNAL_SERVER_ERROR;
				case SERVICE_UNAVAILABLE, GATEWAY_TIMEOUT -> errorEnum = ErrorCodeException.SERVICE_UNAVAILABLE;
				default -> errorEnum = ErrorCodeException.UNKNOWN_ERROR;
			}

			formattedMessage = buildMessage(errorEnum, locale,
					"HTTP Error: %s - %s (%s)", status.value());

			logger.error("{} - {} : HTTP exception occurred: status={}, errorCode={}, message={}",
					CLASS, Utility.getMethodName(), status.value(), errorEnum.getCode(), e.getMessage());

		} else if (e instanceof ResourceAccessException) {

			errorEnum = ErrorCodeException.REST_CONNECTION_ERROR;

			formattedMessage = buildMessage(errorEnum, locale,
					"Network Error: %s (%s)");

			logger.error("{} - {} : Network exception occurred: message={}",
					CLASS, Utility.getMethodName(), e.getMessage());

		} else {

			errorEnum = ErrorCodeException.UNKNOWN_ERROR;

			formattedMessage = buildMessage(errorEnum, locale,
					"Unexpected Error: %s (%s)");

			logger.error("{} - {} : Unexpected exception occurred: message={}",
					CLASS, Utility.getMethodName(), e.getMessage());
		}

		return AppUtil.createApiResponse(false, formattedMessage, null);
	}

	private String buildMessage(ErrorCodeException errorEnum, Locale locale, String format, Object... args) {

		String errorMessage = messageSource.getMessage(
				errorEnum.getMessageKey(),
				null,
				locale
		);

		return String.format(format, args.length > 0
				? new Object[]{args[0], errorMessage, errorEnum.getCode()}
				: new Object[]{errorMessage, errorEnum.getCode()});
	}


	public OrgnizationServiceException orgnizationServiceException(String messageKey) {
        String message = messageSource.getMessage(messageKey, null, Locale.ENGLISH);
        return new OrgnizationServiceException(message);
    }

	public OrgnizationServiceException orgnizationServiceCustomrException(String messageKey) {
        return new OrgnizationServiceException(messageKey);
    }


	public ApiResponses createSuccessResponse(String successMessage, Object result) {
		Locale locale = LocaleContextHolder.getLocale();

		ApiResponses response = new ApiResponses();
		String successMeg = messageSource.getMessage(successMessage, null, locale);
		response.setSuccess(true);
		response.setMessage(successMeg);
		response.setResult(result);
		return response;
	}

		public ApiResponses createErrorResponse(String messageKey) {
		Locale locale = LocaleContextHolder.getLocale();
		String errorMessage = messageSource.getMessage(messageKey, null,locale);
		logger.error("Error response created with message: {}", errorMessage);
		return AppUtil.createApiResponse(false, errorMessage, null);
	}




}
