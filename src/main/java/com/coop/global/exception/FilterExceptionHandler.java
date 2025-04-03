package com.coop.global.exception;

import com.coop.global.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FilterExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> error = new ApiResponse<>(false, null, message);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
