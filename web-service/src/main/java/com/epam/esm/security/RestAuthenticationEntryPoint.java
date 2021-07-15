package com.epam.esm.security;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.Model;
import com.epam.esm.service.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);
    /**
     * Override method 'commence' to return custom json object of error.
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        logger.error("Responding with unauthorized error. Message - {}", e.getMessage());
        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonResult<Model> result = new JsonResult.Builder<Model>()
                .withSuccess(false)
                .withErrorCode("40190")
                .withMessage(e.getMessage())
                .build();
        mapper.writeValue(response.getOutputStream(), result);

    }
}