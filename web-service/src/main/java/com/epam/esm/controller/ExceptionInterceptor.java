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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {
    private static Logger logger = LogManager.getLogger(ExceptionInterceptor.class);

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
        logger.error(message);
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

    private HttpStatus getStatus(String errorCode) {
        if (errorCode != null && errorCode.length() >= 3) {
            int status = Integer.getInteger(errorCode.substring(0, 4));
            switch (status) {
                case 400:
                    return HttpStatus.BAD_REQUEST;
                case 401:
                    return HttpStatus.UNAUTHORIZED;
                case 404:
                    return HttpStatus.NOT_FOUND;
                case 406:
                    return HttpStatus.NOT_ACCEPTABLE;
                case 409:
                    return HttpStatus.CONFLICT;
                case 422:
                    return HttpStatus.UNPROCESSABLE_ENTITY;
                case 500:
                    return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return HttpStatus.NOT_FOUND;
    }
}
