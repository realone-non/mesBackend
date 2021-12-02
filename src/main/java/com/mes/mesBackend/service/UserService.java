package com.mes.mesBackend.service;

import com.mes.mesBackend.auth.TokenResponse;
import com.mes.mesBackend.auth.TokenRequest;
import com.mes.mesBackend.dto.request.UserLogin;
import com.mes.mesBackend.dto.request.UserRequest;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.CustomJwtException;
import com.mes.mesBackend.exception.ExpiredJwtException;
import com.mes.mesBackend.exception.NotFoundException;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {

    // 직원(작업자) 생성
    UserResponse createUser(UserRequest userRequest) throws NotFoundException, NoSuchAlgorithmException, BadRequestException;

    // 직원(작업자) 단일 조회
    UserResponse getUser(Long id) throws NotFoundException;

    // 직원(작업자) 전체 조회 검색조건: 부서, 사번, 이름
    List<UserResponse> getUsers(Long departmentId, String userCode, String korName);

    // 직원(작업자) 페이징 조회
//    Page<UserResponse> getUsers(Pageable pageable);

    // 직원(작업자) 수정
    UserResponse updateUser(Long id, UserRequest userRequest) throws NotFoundException, NoSuchAlgorithmException;

    // 직원(작업자) 삭제
    void deleteUser(Long id) throws NotFoundException;

    // userLogin
    TokenResponse getLogin(UserLogin userLogin) throws NotFoundException, BadRequestException;

    User getUserOrThrow(Long id) throws NotFoundException;

    TokenResponse reissue(TokenRequest tokenRequestDto) throws ExpiredJwtException, CustomJwtException;
}
