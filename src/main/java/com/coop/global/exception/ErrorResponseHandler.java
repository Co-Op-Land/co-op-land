package com.coop.global.exception;

import com.coop.global.common.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 필터 / 서블릿 / 시큐리티 에러 핸들러
 */
@Component
public class ErrorResponseHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void send(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        send(response, ErrorResponse.of(errorCode));
    }

    private void send(HttpServletResponse response, ErrorResponse error) throws IOException {
        response.setStatus(response.getStatus());
        if (error.code().equals("SW401")) { //웹소켓에러
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getOutputStream(), error);
        response.getOutputStream().flush();
    }
}
