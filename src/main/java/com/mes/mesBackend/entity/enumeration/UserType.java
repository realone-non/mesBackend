package com.mes.mesBackend.entity.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

// 유저유형
/*
* 대표: CEO(ROLE_CEO) level: 1
* 관리자: ADMIN(ROLE_ADMIN) level 2
* 매니저: MANAGER(ROLE_MANAGER) level 3
* 부매니저: SUB_MANAGER(ROLE_SUB_MANAGER) level 4
* 일반: NORMAL(ROLE_NORMAL) level 5
* 대기(관리자 승인 x): NEW(ROLE_NEW) level 6
* */
@RequiredArgsConstructor
public enum UserType implements GrantedAuthority {
    CEO("ROLE_CEO", 1),     // 대표
    ADMIN("ROLE_ADMIN", 2), // 관리자
    MANAGER("ROLE_MANAGER", 3), // 매니저
    SUB_MANAGER("ROLE_SUB_MANAGER", 4), // 부매니저
    NORMAL("ROLE_NORMAL", 5),   // 일반
    NEW("ROLE_NEW", 6);         // 대기

    @Getter
    private final String type;

    @Getter
    private final int level;

    @Override
    public String getAuthority() {
        return this.type;
    }
}
