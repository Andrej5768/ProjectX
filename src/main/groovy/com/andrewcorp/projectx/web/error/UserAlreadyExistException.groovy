package com.andrewcorp.projectx.web.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@ResponseStatus(HttpStatus.CONFLICT)
final class UserAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public UserAlreadyExistException() {
        super();
    }

    public UserAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistException(final String message) {
        super(message);
    }

    public UserAlreadyExistException(final Throwable cause) {
        super(cause);
    }

}
