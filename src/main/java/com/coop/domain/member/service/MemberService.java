package com.coop.domain.member.service;

import com.coop.domain.member.entity.Member;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.presentation.member.dto.request.UpdatePasswordRequest;
import com.coop.presentation.member.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberComponent memberComponent;

    /**
     * member 정보 조회
     */
    public MemberResponse readUser(Long id) {
        Member member = memberComponent.findById(id);
        return MemberResponse.from(id, member.getEmail(), member.getNickname());
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void updatePassword(Long id, UpdatePasswordRequest requestDto) {
        Member member = memberComponent.findById(id);
        if(!passwordEncoder.matches(requestDto.oldPassword(), member.getPassword())) {
            throw new InvalidRequestException(ErrorCode.INVALID_PASSWORD);
        }
        member.updatePassword(passwordEncoder.encode(requestDto.newPassword()));
    }
}
