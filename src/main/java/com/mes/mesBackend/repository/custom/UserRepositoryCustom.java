package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.UserAuthorityResponse;
import com.mes.mesBackend.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    // 직원(작업자) 전체 조회 검색조건: 부서, 사번, 이름
    List<User> findAllCondition(Long departmentId, String userCode, String korName);

    // ======================================= 18-2. 권한등록 =======================================
    // 사용자 리스트 검색 조회
    // 검색조건: 사용자 ID(userCode), 이름
    List<UserAuthorityResponse> findUserAuthorityResponsesByCondition(String userCode, String userName);
}
