package com.tourverse.backend.common.exceptions;

public class PaymentException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PaymentException(String message, Throwable cause) {
		super(message, cause);
	}
}