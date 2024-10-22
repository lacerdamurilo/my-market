package br.edu.ifrs.restinga.market.mymarket.config.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        final var className = ex.getStackTrace()[0].getClassName().split("\\.");
        final var lineNumber = ex.getStackTrace()[0].getLineNumber();
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                String.format("%s:%s", className[className.length - 1], lineNumber),
                INTERNAL_SERVER_ERROR.value()
        );

        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        final var className = ex.getStackTrace()[0].getClassName().split("\\.");
        final var lineNumber = ex.getStackTrace()[0].getLineNumber();
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ex.getReason(),
                String.format("%s:%s", className[className.length - 1], lineNumber),
                ex.getStatusCode().value()
        );
        logger.error("NOT FOUND", ex);
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        final var className = ex.getStackTrace()[0].getClassName().split("\\.");
        final var lineNumber = ex.getStackTrace()[0].getLineNumber();
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "Null pointer",
                String.format("%s:%s", className[className.length - 1], lineNumber),
                INTERNAL_SERVER_ERROR.value()
        );
        logger.error("NULL POINTER", ex);
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        final var messageList = Arrays.stream(
                Objects.requireNonNull(ex.getDetailMessageArguments())
        ).map(Object::toString).filter(s -> !s.isBlank()).toList();
        final var message = String.join("; ", messageList);
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ex.getBody().getDetail(),
                message,
                BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(UnsatisfiedServletRequestParameterException ex, WebRequest request) {
        final var missingParams = Arrays.stream(ex.getParamConditions())
                .filter(s -> !request.getParameterMap().containsKey(s)).toList();
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "Bad request",
                String.format("Missing parameters: %s", missingParams),
                BAD_REQUEST.value()
        );
        logger.error("UNSATISFIED PARAMETERS", ex);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

}