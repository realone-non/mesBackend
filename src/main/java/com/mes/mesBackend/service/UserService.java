package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.UserRequest;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.NoSuchAlgorithmException;

public interface UserService {

    // 직원(작업자) 생성
    UserResponse createUser(UserRequest userRequest) throws NotFoundException, NoSuchAlgorithmException, BadRequestException;

    // 직원(작업자) 단일 조회
    UserResponse getUser(Long id) throws NotFoundException;

    // 직원(작업자) 페이징 조회
    Page<UserResponse> getUsers(Pageable pageable);

    // 직원(작업자) 수정
    UserResponse updateUser(Long id, UserRequest userRequest) throws NotFoundException, NoSuchAlgorithmException;

    // 직원(작업자) 삭제
    void deleteUser(Long id) throws NotFoundException;

    // userLogin
    UserResponse.idAndKorNameAndEmail getLogin(String userCode, String password) throws NotFoundException, NoSuchAlgorithmException, BadRequestException;

    User getUserOrThrow(Long id) throws NotFoundException;
}
