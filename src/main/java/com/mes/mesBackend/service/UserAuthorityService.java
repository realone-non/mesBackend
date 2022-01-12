package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.UserAuthorityResponse;

import java.util.List;

// 18-2. 권한등록
public interface UserAuthorityService {
    // 사용자 리스트 검색 조회
    // 검색조건: 사용자 ID(userCode), 이름
    List<UserAuthorityResponse> getUserAuthorities(String userCode, String userName);
}
