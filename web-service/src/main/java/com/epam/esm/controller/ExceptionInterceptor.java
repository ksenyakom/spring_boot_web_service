package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.Entity;
import com.epam.esm.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionInterceptor {
    private static Logger logger = LogManager.getLogger(ExceptionInterceptor.class);

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody JsonResult<Entity> serviceError(final ServiceException e) {
        logger.error("Error code:{}. Error message:{}", e.getErrorCode(), e.getMessage(), e);
        return new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(e.getMessage())
                .withErrorCode(e.getErrorCode())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    public @ResponseBody JsonResult<Entity> otherError(final RuntimeException e) {
        logger.error("Error message:{}", e.getMessage(), e);
        return new JsonResult.Builder<>()
                .withSuccess(false)
                .withMessage(e.getClass() + " " + e.getMessage())
                .build();
    }

}
