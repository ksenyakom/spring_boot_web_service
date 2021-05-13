package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.Model;
import com.epam.esm.service.ServiceException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionInterceptor {
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    JsonResult<Model> messageNotReadable(final HttpMessageNotReadableException e) {
        logger.error(e);

        String message = "Message can not be read.Please, check fields and values." + e.getMessage();
        if (e.getCause().getClass() == UnrecognizedPropertyException.class) {
            message = String.format(e.getMessage()+"Message can not be read. Unrecognized property : %s.", ((UnrecognizedPropertyException) e.getCause()).getPropertyName());
        }
        return new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(message)
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    JsonResult<Model> otherError(final RuntimeException e) {
        logger.error(e);
        return new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(e.getClass() + " " + e.getMessage())
                .build();
    }

}
