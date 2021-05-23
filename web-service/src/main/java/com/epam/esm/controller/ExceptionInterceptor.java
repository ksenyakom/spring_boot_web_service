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
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {
    private static Logger logger = LogManager.getLogger(ExceptionInterceptor.class);

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody
    JsonResult<Model> serviceError(final ServiceException e) {
        logger.error("Error code:{}. Error message:{}", e.getErrorCode(), e.getMessage(), e);
        return new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(e.getMessage())
                .withErrorCode(e.getErrorCode())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        String message = "Method not supported.";
        JsonResult<?> result = new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(message)
                .build();

        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);

        String message = "Message can not be read.Please, check fields and values. Error:" + ex.getMessage();
        if (ex.getCause().getClass() == UnrecognizedPropertyException.class) {
            message = String.format("Message can not be read. Unrecognized property : %s.", ((UnrecognizedPropertyException) ex.getCause()).getPropertyName());
        }
        JsonResult<?> result = new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(message)
                .build();

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
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
                .withMessage("Failed to convert parameter value.")
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

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        JsonResult<?> result = new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(ex.getClass() + " " +ex.getMessage())
                .build();
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }



}
