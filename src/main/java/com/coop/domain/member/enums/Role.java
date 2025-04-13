package com.coop.domain.member.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.List;

public enum Role {
    ADMIN, USER;

    /**
     * security context 에 삽입하기 위한 메서드
     */
    public List<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_" + this.name());
    }
}
