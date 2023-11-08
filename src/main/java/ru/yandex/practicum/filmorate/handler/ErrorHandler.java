package ru.yandex.practicum.filmorate.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        log.error("ConstraintViolationException: message {}/n stackTrace: {}", e.getMessage(), e.getStackTrace());
        return new ErrorResponse(e.getMessage(), getStackTrace(e));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error("ValidationException: message {}/n stackTrace: {}", e.getMessage(), e.getStackTrace());
        return new ErrorResponse(e.getMessage(), getStackTrace(e));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectDoesNotExistException(final NotFoundException e) {
        log.error("ObjectDoesNotExistException: message {}/n stackTrace: {}", e.getMessage(), e.getStackTrace());
        return new ErrorResponse(e.getMessage(), getStackTrace(e));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Exception e) {
        log.error("Throwable: message {}/n stackTrace: {}", e.getMessage(), e.getStackTrace());
        return new ErrorResponse(e.getMessage(), getStackTrace(e));
    }
}