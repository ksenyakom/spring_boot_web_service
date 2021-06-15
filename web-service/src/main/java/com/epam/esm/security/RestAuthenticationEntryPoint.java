package com.epam.esm.security;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.model.Model;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * Override method 'commence' to return custom json object of error.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonResult<Model> result = new JsonResult.Builder<Model>()
                .withSuccess(false)
                .withErrorCode("40190")
                .withMessage(authenticationException.getMessage())
                .build();
        mapper.writeValue(response.getOutputStream(), result );
    }
}