package com.example.apifamilia.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	Map<String, Object> handleIllegalArgument(IllegalArgumentException exception) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("error", "bad_request");
		response.put("message", exception.getMessage());
		return response;
	}
}
