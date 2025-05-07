package com.coop.global.security;

import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.ErrorResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private final ErrorResponseHandler errorResponseHandler;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        errorResponseHandler.send(
                response,
                ErrorCode.ACCESS_DENIED
        );
    }
}
