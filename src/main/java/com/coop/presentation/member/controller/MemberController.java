package com.coop.presentation.member.controller;

import com.coop.domain.member.service.MemberService;
import com.coop.global.common.ApiResponse;
import com.coop.presentation.member.dto.request.UpdatePasswordRequest;
import com.coop.presentation.member.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<ApiResponse<MemberResponse>> readUser(
            @AuthenticationPrincipal User userDetails
    ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        MemberResponse responseDto = memberService.readUser(memberId);
        return ApiResponse.success(responseDto);
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @AuthenticationPrincipal User userDetails,
            @RequestBody UpdatePasswordRequest requestDto
    ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        memberService.updatePassword(memberId, requestDto);
        return ApiResponse.success("비밀번호 변경 성공");
    }
}
