package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.UserAuthorityResponse;
import com.mes.mesBackend.entity.enumeration.UserType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

// 18-2. 권한등록
public interface UserAuthorityService {
    // 사용자 리스트 검색 조회
    // 검색조건: 사용자 ID(userCode), 이름
    List<UserAuthorityResponse> getUserAuthorities(String userCode, String userName);
    // 유저 권한 생성, 권한 NEW 로는 생성 할 수 없음
    UserAuthorityResponse createUserAuthority(Long userId, UserType userType) throws NotFoundException, BadRequestException;
    // 유저 권한 삭제, 권한이 NEW 로 변경
    void deleteUserAuthority(Long userId) throws NotFoundException, BadRequestException;
}
