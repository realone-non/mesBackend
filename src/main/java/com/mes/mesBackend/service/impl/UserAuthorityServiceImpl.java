package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.UserAuthorityResponse;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.enumeration.UserType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
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

    // 유저 권한 생성, 권한 NEW 로는 생성 할 수 없음
    @Override
    public UserAuthorityResponse createUserAuthority(Long userId, UserType userType) throws NotFoundException, BadRequestException {
        if (userType.equals(UserType.NEW)) throw new BadRequestException("대기 권한으로는 등록을 진행할 수 없습니다.");
        User user = getUserOrThrow(userId);
//        throwIfUserTypeNeNew(user.getUserType());
        user.setUserType(userType);
        userRepo.save(user);
        return getUSerAuthorityResponseOrThrow(user.getId());
    }

    // 유저 권한 삭제, 권한이 NEW 로 변경
    @Override
    public void deleteUserAuthority(Long userId) throws NotFoundException, BadRequestException {
        User user = getUserOrThrow(userId);
        throwIfUserTypeEqNew(user.getUserType());
        user.setUserType(UserType.NEW);
        userRepo.save(user);
    }

    private UserAuthorityResponse getUSerAuthorityResponseOrThrow(Long id) throws NotFoundException {
        return userRepo.findUserAuthorityResponsesByUserId(id)
                .orElseThrow(() -> new NotFoundException("user does not exist. input id: " + id));
    }

    // NEW 권한 유저 단일 조회 및 예외
    private User getUserOrThrow(Long id) throws NotFoundException, BadRequestException {
        return userRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("user does not exist. input id: " + id));
    }

    private void throwIfUserTypeNeNew(UserType userType) throws BadRequestException {
        if (!userType.equals(UserType.NEW)) {
            throw new BadRequestException("해당 유저의 권한이 대기 권한이 아니므로 등록이 불가능 합니다. ");
        }
    }

    private void throwIfUserTypeEqNew(UserType userType) throws BadRequestException {
        if (userType.equals(UserType.NEW)) {
            throw new BadRequestException("해당 유저의 권한이 대기 권한이므로 삭제 불가능 합니다. ");
        }
    }
}
