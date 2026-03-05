package com.dtt.organization.util;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.dtt.organization.constant.ApiResponses;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.i18n.LocaleContextHolder;

public class Utility {
	
	private MessageSource messageSource;

	Locale locale = LocaleContextHolder.getLocale();
	
	public Utility(MessageSource messageSource) {
		super();
		this.messageSource = messageSource;
	}

	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Utility method to get the current method name dynamically.
	 * 
	 * @return The name of the current method.
	 */
	public static String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
	
    /**
     * Converts an object to a JSON string.
     * @param object The object to convert.
     * @return The object as a JSON string.
     * @throws JsonProcessingException if there is an error during serialization.
     */
    public static String convertToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
    
 // Helper method to create success responses
    public ApiResponses createSuccessResponse(String messageKey, Object data) {
        String message = messageSource.getMessage(messageKey, null, locale);
        return AppUtil.createApiResponse(true, message, data);
    }
}
