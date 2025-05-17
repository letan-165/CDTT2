package com.app.QuizService.Config;

import com.app.QuizService.DTO.ApiResponse;
import com.app.QuizService.Exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class AuthEntryPointCustom implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.AUTHENTICATION;
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getHttpStatusCode().value());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                .build());

        response.getWriter().write(json);
        response.flushBuffer();
    }
}
