package com.mes.mesBackend.repository.impl;

import com.mes.mesBackend.entity.QUser;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.repository.custom.UserRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    final QUser user = QUser.user;

    // 직원(작업자) 전체 조회 검색조건: 부서, 사번, 이름
    @Override
    public List<User> findAllCondition(Long departmentId, String userCode, String korName) {
        return jpaQueryFactory
                .selectFrom(user)
                .where(
                        isDepartmentEq(departmentId),
                        isUserCodeContaining(userCode),
                        isKorNameContaining(korName),
                        isDeleteYnFalse()
                )
                .fetch();
    }

    // 부서로 검색
    private BooleanExpression isDepartmentEq(Long departmentId) {
        return departmentId != null ? user.department.id.eq(departmentId) : null;
    }

    // 사번으로 검색
    private BooleanExpression isUserCodeContaining(String userCode) {
        return userCode != null ? user.userCode.contains(userCode) :  null;
    }

    // 이름으로 검색
    private BooleanExpression isKorNameContaining(String korName) {
        return korName != null ? user.korName.contains(korName) : null;
    }

    private BooleanExpression isDeleteYnFalse() {
        return user.deleteYn.isFalse();
    }
}
