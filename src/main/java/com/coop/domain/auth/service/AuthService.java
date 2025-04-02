package com.coop.domain.auth.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.member.repository.MemberRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.global.exception.error.UnAuthorizedException;
import com.coop.global.security.JwtSecurityProperties;
import com.coop.global.security.JwtUtil;
import com.coop.presentation.auth.dto.request.LoginRequest;
import com.coop.presentation.auth.dto.request.SignupRequest;
import com.coop.presentation.auth.dto.response.LoginResponse;
import com.coop.presentation.auth.dto.response.RefreshAccessTokenResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtSecurityProperties jwtSecurityProperties;
    private final RefreshTokenService refreshTokenService;
    private final BlackListService blackListService;

    /**
     * 회원가입
     */
    public void signup(SignupRequest dto) {
        if (memberRepository.existsByEmail(dto.email())) {
            throw new InvalidRequestException(ErrorCode.ALREADY_EXIST_EMAIL);
        }
        String encodedPassword = passwordEncoder.encode(dto.password());
        Member member = SignupRequest.toEntity(
                dto.email(),
                dto.nickname(),
                encodedPassword
        );
        memberRepository.save(member);
    }

    /**
     * 로그인: AccessToken, RefreshToken 생성
     */
    public LoginResponse login(LoginRequest dto) {
        Member member = memberRepository.findByEmail(dto.email())
                .orElseThrow();
        if (!passwordEncoder.matches(dto.password(), member.getPassword())) {
            throw new InvalidRequestException(ErrorCode.INVALID_PASSWORD);
        }
        String accessToken = jwtUtil.createToken(member.getId(), member.getRole());
        String refreshToken = jwtUtil.createRefreshToken(member.getId());

        refreshTokenService.createRefreshToken(member.getId(), refreshToken);
        return LoginResponse.from(accessToken, refreshToken);
    }

    /**
     * 로그아웃: Redis 의 블랙리스트에 AccessToken 을 등록
     */
    public void logout(String authHeader) {
        String prefix = jwtSecurityProperties.token().prefix();
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(prefix)) {
            throw new UnAuthorizedException(ErrorCode.TOKEN_UNAUTHORIZED);
        }
        String token = jwtUtil.substringToken(authHeader);
        Claims claims = jwtUtil.extractClaims(token);
        long expirationMillis = claims.getExpiration().getTime() - System.currentTimeMillis();
        if (expirationMillis > 0) {
            blackListService.addToBlackList(token);
        }
        String[] accessTokenData = claims.getSubject().split(":");
        Long userId = Long.valueOf(accessTokenData[0]);
        refreshTokenService.deleteRefreshToken(userId);
    }

    /**
     * AccessToken 재발급: Refresh Token Rotation 적용 됨
     */
    public RefreshAccessTokenResponse refreshAccessToken(String refreshToken) {
        refreshToken = jwtUtil.substringToken(refreshToken);
        Claims claims;
        try {
            claims = jwtUtil.extractClaims(refreshToken);
        } catch (Exception e) {
            throw new UnAuthorizedException(ErrorCode.TOKEN_UNAUTHORIZED);
        }
        Long memberId = Long.parseLong(claims.getSubject());
        String storedRefreshToken = refreshTokenService.getRefreshToken(memberId);

        if (!refreshToken.equals(jwtUtil.substringToken(storedRefreshToken))) {
            throw new UnAuthorizedException(ErrorCode.TOKEN_UNAUTHORIZED);
        }
        String newRefreshToken = jwtUtil.createRefreshToken(memberId);
        refreshTokenService.createRefreshToken(memberId, newRefreshToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow();
        String refreshAccessToken = jwtUtil.createToken(memberId, member.getRole());
        return RefreshAccessTokenResponse.from(refreshAccessToken, newRefreshToken);
    }
}
