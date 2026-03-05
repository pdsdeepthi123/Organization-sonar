package com.dtt.organization.restcontroller;

import java.io.IOException;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.dtt.organization.constant.ApiResponses;
import com.dtt.organization.util.AppUtil;
import com.fasterxml.jackson.core.JsonProcessingException;


@ControllerAdvice
public class GlobalExceptionHandler {

	// 1. Handle file size exceeded and other file-related exceptions
    @ExceptionHandler({MaxUploadSizeExceededException.class, FileSizeLimitExceededException.class, IOException.class})
    public ResponseEntity<ApiResponses> handleMaxSizeException(Exception ex) {
    	String message = "Error uploading file. ";

        if (ex instanceof MaxUploadSizeExceededException) {
            message += "Maximum upload size exceeded. File must be smaller than 50 MB.";
        } else if (ex instanceof FileSizeLimitExceededException) {
            message += "File exceeds its maximum permitted size.";
        } else if (ex instanceof IOException) {
            message += "There was an issue processing the file.";
        } else {
            message += "Please check the file and try again.";
        }
        ApiResponses response = AppUtil.createApiResponse(false,message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ApiResponses> handleJsonProcessingException(JsonProcessingException ex) {
        String message = "There was an error processing your request. Please ensure the provided data is in the correct format.";
        ApiResponses response = AppUtil.createApiResponse(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    // Handle missing request parameters
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponses> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String message = "Required parameter is missing: " + ex.getParameterName();
        ApiResponses response = AppUtil.createApiResponse(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
 // Handle Unsupported HTTP Request Methods
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponses> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String message = "HTTP method " + ex.getMethod() + " is not supported for this endpoint.";
        ApiResponses response = AppUtil.createApiResponse(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }
    
 // Handle Unsupported Media Type in Request Body
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponses> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        String message = "The requested media type " + ex.getContentType() + " is not supported.";
        ApiResponses response = AppUtil.createApiResponse(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    
 // Handle Not Acceptable Media Type
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiResponses> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        String message = "The requested media type is not acceptable.";
        ApiResponses response = AppUtil.createApiResponse(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }
    
 // Handle missing handler (URL not found)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponses> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String message = "No handler found for the given request.";
        ApiResponses response = AppUtil.createApiResponse(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
 // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponses> handleGenericException(Exception ex) {
        String message = "An unexpected error occurred: " + ex.getMessage();
        ApiResponses response = AppUtil.createApiResponse(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponses> handleValidationExceptions(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(org.springframework.validation.FieldError::getDefaultMessage) // method reference
                .orElse("Validation error");

        return ResponseEntity
                .badRequest()
                .body(AppUtil.createApiResponse(false, errorMessage, null));
    }
}

