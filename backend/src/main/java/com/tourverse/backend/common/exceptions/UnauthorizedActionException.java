package com.tourverse.backend.common.exceptions;

public class UnauthorizedActionException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnauthorizedActionException(String message) {
        super(message);
    }
}
