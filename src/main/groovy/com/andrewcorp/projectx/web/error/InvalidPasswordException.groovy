package com.andrewcorp.projectx.web.error

import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.http.HttpStatus

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
class InvalidPasswordException extends RuntimeException {
    InvalidPasswordException(String s) {
    }
}
