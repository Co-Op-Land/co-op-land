package com.coop.domain.member.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.List;

public enum Role {
    ADMIN, USER;

    public List<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_" + this.name());
    }
}
