package com.user.app.commons;

import com.user.app.commons.dto.ApiResponse;
import com.user.app.exceptions.ApiException;
import com.user.app.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(ex.getCause().toString(),message, false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex) {
		Map<String, String> resp = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			resp.put(fieldName, message);
		});

		return new ResponseEntity<Map<String, String>>(resp, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, String>> accessDeniedException(
			AccessDeniedException ex) {
		Map<String, String> resp = new HashMap<>();
		resp.put("message", ex.getMessage());
		resp.put("cause", String.valueOf(ex.getCause()));
		return new ResponseEntity<Map<String, String>>(resp, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(IndexOutOfBoundsException.class)
	public ResponseEntity<Map<String, String>> indexOutOfBoundsException(
			IndexOutOfBoundsException ex) {
		Map<String, String> resp = new HashMap<>();
		resp.put("message", ex.getMessage());
		resp.put("cause", String.valueOf(ex.getCause()));
		return new ResponseEntity<Map<String, String>>(resp, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse> handleApiException(ApiException ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(ex.getCause().toString(),message, true);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleAllException(Exception ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(ex.getCause().toString(),message, true);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
	}



}
