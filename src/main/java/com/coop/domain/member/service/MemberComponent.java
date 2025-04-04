package com.coop.domain.member.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.member.repository.MemberRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MemberComponent {

    private final MemberRepository memberRepository;

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow();
    }

    public Member findMemberReference(Long id) {
        return memberRepository.getReferenceById(id);
    }

    public List<Member> getMembers(Set<Long> playerInRoom) {
        return memberRepository.findMembersByIds(playerInRoom);
    }

    public void isExist(Long id) {
        if (memberRepository.existsById(id)) {
            throw new InvalidRequestException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    public void isExist(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new InvalidRequestException(ErrorCode.ALREADY_EXIST_EMAIL);
        }
    }
}