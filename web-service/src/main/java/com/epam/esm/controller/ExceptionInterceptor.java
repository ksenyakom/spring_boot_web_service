package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.Model;
import com.epam.esm.service.ServiceException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.security.core.AuthenticationException;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {
    private static Logger logger = LogManager.getLogger(ExceptionInterceptor.class);
    private static final String Status_500 = "01 02 03 04 05 11 12 14 15 16 17 18 22 23 25 26 33 35 36 37 38 40 51 52 61 63 65 64 66 68 50050";
    private static final String Status_422 = "42220 41 42 43 53 67 42283";
    private static final String Status_400 = "27 40024 34";
    private static final String Status_406 = "40629 69";
    private static final String Status_409 = "40919, 40955";
    private static final String Status_401 = "40190";

    private HttpStatus getStatus(String errorCode) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        if (Status_401.contains(errorCode)) {
            status = HttpStatus.UNAUTHORIZED;
        }
        if (Status_409.contains(errorCode)) {
            status = HttpStatus.CONFLICT;
        }
        if (Status_422.contains(errorCode)) {
            status = HttpStatus.UNPROCESSABLE_ENTITY;
        }
        if (Status_406.contains(errorCode)) {
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        if (Status_400.contains(errorCode)) {
            status = HttpStatus.BAD_REQUEST;
        }
        if (Status_500.contains(errorCode)) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return status;
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> serviceError(final ServiceException e) {
        logger.error("Error code:{}. Error message:{}", e.getErrorCode(), e.getMessage(), e);
        HttpStatus status = getStatus(e.getErrorCode());

        JsonResult<?> result = new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(e.getMessage())
                .withErrorCode(e.getErrorCode())
                .build();
        return new ResponseEntity<>(result, status);

    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        String message = "Method not supported.";
        JsonResult<?> result = new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(message)
                .build();

        return new ResponseEntity<>(result, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        String message = "Message can not be read.Please, check fields and values. Error:" + ex.getMessage();
        if (ex.getCause() != null && ex.getCause().getClass() == UnrecognizedPropertyException.class) {
            message = String.format("Message can not be read. Unrecognized property : %s.", ((UnrecognizedPropertyException) ex.getCause()).getPropertyName());
            status = HttpStatus.UNPROCESSABLE_ENTITY;
        }
        JsonResult<?> result = new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(message)
                .build();

        return new ResponseEntity<>(result, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        String message = String.format("Required request parameter '%s' for method parameter is not present", ex.getParameterName());
        JsonResult<?> result = new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(message)
                .build();

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = String.format("Page not found: %s", ex.getRequestURL());
        JsonResult<?> result = new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(message)
                .build();

        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    JsonResult<Model> constraintViolationError(final ConstraintViolationException e) {
        logger.error(e);
        return new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    JsonResult<Model> methodArgumentViolationError(final MethodArgumentTypeMismatchException e) {
        logger.error(e);

        return new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(String.format("Failed to convert parameter value: %s.", e.getParameter().getParameterName()))
                .build();
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    JsonResult<Model> errorSQLException(final SQLIntegrityConstraintViolationException e) {
        logger.error(e);

        return new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public @ResponseBody
    JsonResult<Model> errorAuthenticationException(final AccessDeniedException e) {
        logger.error(e);

        return new JsonResult.Builder<>()
                .withSuccess(false)
                .withErrorCode("40391")
                .withMessage("Access denied")
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        JsonResult<?> result = new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(ex.getClass() + " " + ex.getMessage())
                .build();
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }


}
