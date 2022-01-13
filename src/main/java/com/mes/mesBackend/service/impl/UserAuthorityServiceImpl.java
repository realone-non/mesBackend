package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.UserAuthorityResponse;
import com.mes.mesBackend.repository.UserRepository;
import com.mes.mesBackend.service.UserAuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 18-2. 권한등록
@Service
@RequiredArgsConstructor
public class UserAuthorityServiceImpl implements UserAuthorityService {
    private final UserRepository userRepo;
    // 사용자 리스트 검색 조회
    // 검색조건: 사용자 ID(userCode), 이름
    @Override
    public List<UserAuthorityResponse> getUserAuthorities(String userCode, String userName) {
        return userRepo.findUserAuthorityResponsesByCondition(userCode, userName);
    }
}
