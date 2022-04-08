package com.mes.mesBackend.entity.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

// 유저유형
/*
* 대기(관리자 승인 x): NEW(ROLE_NEW)
* 일반: NORMAL(ROLE_NORMAL)
* 관리자: ADMIN(ROLE_ADMIN)
* 부서장: MANAGER(ROLE_MANAGER)
* 대표: CEO(ROLE_CEO)
* */
@RequiredArgsConstructor
public enum UserType implements GrantedAuthority {
    CEO("ROLE_CEO", 1),
    MANAGER("ROLE_MANAGER", 2),
    ADMIN("ROLE_ADMIN", 3),
    NORMAL("ROLE_NORMAL", 4),
    NEW("ROLE_NEW", 5);

    @Getter
    private final String type;

    @Getter
    private final int level;

    @Override
    public String getAuthority() {
        return this.type;
    }
}
