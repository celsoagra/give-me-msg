package io.gmm.exception;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

public class EntityValidationException extends RuntimeException {

	private String message;

	public EntityValidationException(Set<? extends ConstraintViolation<?>> violations) {
		this.message = violations.stream().map(cv -> cv.getMessage()).collect(Collectors.joining(", "));
	}

	public EntityValidationException() {

	}

	public String getMessage() {
		return message;
	}
}
