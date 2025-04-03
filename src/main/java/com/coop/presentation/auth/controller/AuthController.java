package com.coop.presentation.auth.controller;

import com.coop.domain.auth.service.AuthService;
import com.coop.domain.auth.service.CookieService;
import com.coop.global.common.ApiResponse;
import com.coop.presentation.auth.dto.request.LoginRequest;
import com.coop.presentation.auth.dto.request.SignupRequest;
import com.coop.presentation.auth.dto.response.LoginResponse;
import com.coop.presentation.auth.dto.response.RefreshAccessTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(
            @Valid @RequestBody SignupRequest requestDto
    ) {
        authService.signup(requestDto);
        return ApiResponse.created("회원가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest requestDto,
            HttpServletResponse response
    ) {
        LoginResponse responseDto = authService.login(requestDto);
        cookieService.createRefreshTokenCookie(responseDto.refreshToken(), response);
        LoginResponse newResponseDto = new LoginResponse(responseDto.accessToken(), null);
        return ApiResponse.success(newResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader("Authorization") String authHeader,
            HttpServletResponse response
    ) {
        authService.logout(authHeader);
        cookieService.removeRefreshTokenCookie(response);
        return ApiResponse.success("로그아웃 성공");
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshAccessTokenResponse>> refreshAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = cookieService.getRefreshTokenCookie(request);
        RefreshAccessTokenResponse responseDto = authService.refreshAccessToken(refreshToken);
        cookieService.removeRefreshTokenCookie(response);
        cookieService.createRefreshTokenCookie(responseDto.refreshToken(), response);
        RefreshAccessTokenResponse newResponseDto = new RefreshAccessTokenResponse(responseDto.accessToken(), null);
        return ApiResponse.success(newResponseDto);
    }
}
