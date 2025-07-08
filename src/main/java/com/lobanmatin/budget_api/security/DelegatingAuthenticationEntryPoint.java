package com.lobanmatin.budget_api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lobanmatin.budget_api.exception.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DelegatingAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final GlobalExceptionHandler exceptionHandler;
    private final ObjectMapper objectMapper;

    public DelegatingAuthenticationEntryPoint(GlobalExceptionHandler exceptionHandler,
                                              ObjectMapper objectMapper) {
        this.exceptionHandler = exceptionHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ResponseEntity<?> result = exceptionHandler.handleAuthenticationException(authException);

        response.setStatus(result.getStatusCodeValue());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result.getBody()));
    }
}
