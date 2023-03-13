package com.mes.mesBackend.service;

import com.mes.mesBackend.auth.TokenResponse;
import com.mes.mesBackend.auth.TokenRequest;
import com.mes.mesBackend.dto.request.UserLogin;
import com.mes.mesBackend.dto.request.UserCreateRequest;
import com.mes.mesBackend.dto.request.UserRegistrationRequest;
import com.mes.mesBackend.dto.request.UserUpdateRequest;
import com.mes.mesBackend.dto.response.UserRegistrationResponse;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.enumeration.UserType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.CustomJwtException;
import com.mes.mesBackend.exception.NotFoundException;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {

    // 직원(작업자) 생성
    UserResponse createUser(UserCreateRequest userRequest) throws NotFoundException, NoSuchAlgorithmException, BadRequestException;

    // 직원(작업자) 단일 조회
    UserResponse getUser(Long id) throws NotFoundException;

    // 직원(작업자) 전체 조회 검색조건: 부서, 사번, 이름
    List<UserResponse> getUsers(Long departmentId, String userCode, String korName, UserType userType);

    // 직원(작업자) 페이징 조회
//    Page<UserResponse> getUsers(Pageable pageable);

    // 직원(작업자) 수정
    UserResponse updateUser(Long id, UserUpdateRequest userRequest) throws NotFoundException, NoSuchAlgorithmException, BadRequestException;

    // 직원(작업자) 삭제
    void deleteUser(Long id) throws NotFoundException;

    // userLogin
    TokenResponse getLogin(UserLogin userLogin, String clientIp) throws NotFoundException, BadRequestException;

    User getUserOrThrow(Long id) throws NotFoundException;

    TokenResponse reissue(TokenRequest tokenRequestDto) throws CustomJwtException, NotFoundException;

    // =============================== 18-3. 사용자 등록 ===============================
    // 사용자 생성
    UserRegistrationResponse createUserRegistration(UserRegistrationRequest userRegistrationRequest) throws BadRequestException, NotFoundException;
    // 사용자 단일 조회
    UserRegistrationResponse getUserRegistration(Long id) throws NotFoundException;
    // 사용자 리스트 검색 조회
    // 검색조건: 사용자 ID(사번), 이름, 공장(X)
    List<UserRegistrationResponse> getUserRegistrations(String userCode, String korName);
    // 사용자 수정
    UserRegistrationResponse updateUserRegistration(Long id, UserRegistrationRequest.Update userRegistrationRequest) throws NotFoundException, BadRequestException;
    // 사용자 삭제
    void deleteUserRegistration(Long id) throws NotFoundException;
    // 비밀번호 초기화
    void resetPassword(String email) throws NotFoundException;
    // 비밀번호 변경
    void updatePassword(String userCode, UserCreateRequest.password password) throws NotFoundException, BadRequestException;
}
