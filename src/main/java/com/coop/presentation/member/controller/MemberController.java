package com.coop.presentation.member.controller;

import com.coop.domain.member.service.MemberService;
import com.coop.global.common.ApiResponse;
import com.coop.global.security.AuthUser;
import com.coop.presentation.member.dto.request.UpdatePasswordRequest;
import com.coop.presentation.member.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<ApiResponse<MemberResponse>> readUser(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        MemberResponse responseDto = memberService.readUser(authUser.memberId());
        return ApiResponse.success(responseDto);
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody UpdatePasswordRequest requestDto
    ) {
        memberService.updatePassword(authUser.memberId(), requestDto);
        return ApiResponse.success("비밀번호 변경 성공");
    }
}
