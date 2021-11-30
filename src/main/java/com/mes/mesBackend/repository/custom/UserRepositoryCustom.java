package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    // 직원(작업자) 전체 조회 검색조건: 부서, 사번, 이름
    List<User> findAllCondition(Long departmentId, String userCode, String korName);
}
