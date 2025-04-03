package com.coop.land.auth;

import com.coop.domain.auth.service.CookieService;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.UnAuthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetRefreshTokenCookieTest {

    private final CookieService cookieService = new CookieService();

    @Test
    void getRefreshTokenCookie_성공케이스() {
        String expectedToken = "test-refresh-token";
        Cookie cookie = new Cookie("refreshToken", expectedToken);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        String result = cookieService.getRefreshTokenCookie(request);

        assertEquals(expectedToken, result);
    }

    @Test
    void getRefreshTokenCookie_쿠키없을때() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class,
                () -> cookieService.getRefreshTokenCookie(request));
        assertEquals(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(), exception.getMessage());
    }

    @Test
    void getRefreshTokenCookie_refreshToken없을때() {
        Cookie someOtherCookie = new Cookie("notRefreshToken", "value");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{someOtherCookie});

        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class,
                () -> cookieService.getRefreshTokenCookie(request));
        assertEquals(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(), exception.getMessage());
    }
}
