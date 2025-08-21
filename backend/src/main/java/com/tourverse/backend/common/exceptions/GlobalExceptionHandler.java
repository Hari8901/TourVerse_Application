package com.tourverse.backend.common.exceptions;

import com.tourverse.backend.common.dto.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BookingException.class)
	public ResponseEntity<ErrorDetails> handleBookingException(BookingException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UnauthorizedActionException.class)
	public ResponseEntity<ErrorDetails> handleUnauthorizedActionException(UnauthorizedActionException ex,
			WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorDetails> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorDetails> handleIllegalArgumentException(IllegalArgumentException ex,
			WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorDetails> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ErrorDetails> handlePaymentException(PaymentException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<ErrorDetails> handleFileUploadException(FileUploadException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ItineraryException.class)
	public ResponseEntity<ErrorDetails> handleItineraryException(ItineraryException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails> handleValidationExceptions(MethodArgumentNotValidException ex,
			WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		String message = "Validation failed: " + errors;
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(message)
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> globalExceptionHandler(Exception ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage())
				.details(request.getDescription(false)).build();
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}