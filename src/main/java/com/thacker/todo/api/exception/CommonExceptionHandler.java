package com.thacker.todo.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {
	
	@ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<Object> notFoundException(NotFoundException e) {
		log.debug("Not found", e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Map.of("errorMessage", e.getMessage()));
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.debug("Bad Request", e);
		List<String> messages = Arrays.stream(e.getDetailMessageArguments())
				.map(Object::toString)
				.filter(x -> !x.isEmpty())
				.toList();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Map.of("errorMessage", messages));
	}
}
