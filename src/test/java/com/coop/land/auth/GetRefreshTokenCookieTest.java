package com.coop.land.auth;

import com.coop.domain.auth.service.CookieService;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.UnAuthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetRefreshTokenCookieTest {

    @Mock private HttpServletRequest request;

    @InjectMocks private CookieService cookieService;

    @Test
    void getRefreshTokenCookie_성공케이스() {
        String expectedToken = "test-refresh-token";
        Cookie cookie = new Cookie("refreshToken", expectedToken);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        String result = cookieService.getRefreshTokenCookie(request);

        assertEquals(expectedToken, result);
    }

    @Test
    void getRefreshTokenCookie_쿠키없을때() {
        when(request.getCookies()).thenReturn(null);

        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class,
                () -> cookieService.getRefreshTokenCookie(request));
        assertEquals(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(), exception.getMessage());
    }

    @Test
    void getRefreshTokenCookie_refreshToken없을때() {
        Cookie someOtherCookie = new Cookie("notRefreshToken", "value");
        when(request.getCookies()).thenReturn(new Cookie[]{someOtherCookie});

        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class,
                () -> cookieService.getRefreshTokenCookie(request));
        assertEquals(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(), exception.getMessage());
    }
}