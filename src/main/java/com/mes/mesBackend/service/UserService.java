package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.UserRequest;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    // 직원(작업자) 생성
    UserResponse createUser(UserRequest userRequest) throws NotFoundException;

    // 직원(작업자) 단일 조회
    UserResponse getUser(Long id) throws NotFoundException;

    // 직원(작업자) 페이징 조회
    Page<UserResponse> getUsers(Pageable pageable);

    // 직원(작업자) 수정
    UserResponse updateUser(Long id, UserRequest userRequest) throws NotFoundException;

    // 직원(작업자) 삭제
    void deleteUser(Long id) throws NotFoundException;

    // userLogin
    UserResponse getLogin(String userCode, String password) throws NotFoundException;

    User findUserOrThrow(Long id) throws NotFoundException;
}
