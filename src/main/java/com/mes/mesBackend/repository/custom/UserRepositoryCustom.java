package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.UserAuthorityResponse;
import com.mes.mesBackend.dto.response.UserRegistrationResponse;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.enumeration.UserType;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    // 직원(작업자) 전체 조회 검색조건: 부서, 사번, 이름
    List<User> findAllCondition(Long departmentId, String userCode, String korName, UserType userType);
    // ======================================= 18-2. 권한등록 =======================================
    // 사용자 리스트 검색 조회
    // 검색조건: 사용자 ID(userCode), 이름
    List<UserAuthorityResponse> findUserAuthorityResponsesByCondition(String userCode, String userName);
    // 권한등록 단일 조회
    Optional<UserAuthorityResponse> findUserAuthorityResponsesByUserId(Long userId);
    // ======================================= 18-3. 사용자 등록 =======================================
    // 사용자 리스트 검색 조회
    // 검색조건: 사용자 ID(사번), 이름, 공장(X)
    List<UserRegistrationResponse> findUserRegistrationByCondition(String userCode, String korName);
    // 사용자 단일 조회
    Optional<UserRegistrationResponse> findUserRegistrationByIdAndDeleteYnFalse(Long id);
}
