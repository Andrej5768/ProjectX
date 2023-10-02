package com.andrewcorp.projectx.web.error

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.http.HttpStatus

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@ControllerAdvice
class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource

    RestResponseEntityExceptionHandler() {
        super()
    }

    @ExceptionHandler(value = [UserNotFoundException.class])
    ResponseEntity<?> handleUsernameNotFoundException(Exception ex, WebRequest request) {
        logger.error("Username not found", ex)
        return handleExceptionInternal(ex, messageSource.getMessage("user.not.found", null, request.getLocale()), null, HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(value = [LikeNotFoundException.class])
    ResponseEntity<?> handleLikeNotFoundException(Exception ex, WebRequest request) {
        logger.error("Like not found", ex)
        return handleExceptionInternal(ex, messageSource.getMessage("like.not.found", null, request.getLocale()), null, HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(value = [PostNotFoundException.class])
    ResponseEntity<?> handlePostNotFoundException(Exception ex, WebRequest request) {
        logger.error("Post not found", ex)
        return handleExceptionInternal(ex, messageSource.getMessage("post.not.found", null, request.getLocale()), null, HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(value = [UserAlreadyExistException.class])
    ResponseEntity<?> handleUserAlreadyExistException(Exception ex, WebRequest request) {
        logger.error("User already exists", ex)
        return handleExceptionInternal(ex, messageSource.getMessage("user.already.exists", null, request.getLocale()), null, HttpStatus.CONFLICT, request)
    }

    @ExceptionHandler(value = [InvalidPasswordException.class])\
    ResponseEntity<?> handleInvalidPasswordException(Exception ex, WebRequest request) {
        logger.error("Invalid password", ex)
        return handleExceptionInternal(ex, messageSource.getMessage("invalid.password", null, request.getLocale()), null, HttpStatus.BAD_REQUEST, request)
    }
}
