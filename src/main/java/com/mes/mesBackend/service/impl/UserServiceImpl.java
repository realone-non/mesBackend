package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.UserRequest;
import com.mes.mesBackend.dto.response.UserResponse;
import com.mes.mesBackend.entity.Department;
import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.UserRepository;
import com.mes.mesBackend.service.DepartmentService;
import com.mes.mesBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    ModelMapper mapper;

    public User getUserOrThrow(Long id) throws NotFoundException {
        return userRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("user does not exists. input id: " + id));
    }

    // 직원(작업자) 생성
    public UserResponse createUser(UserRequest userRequest) throws NotFoundException {
        Department department = departmentService.getDepartmentOrThrow(userRequest.getDepartmentId());
        User user = mapper.toEntity(userRequest, User.class);
        user.addJoin(department);
        userRepository.save(user);
        return mapper.toResponse(user, UserResponse.class);
    }

    // 직원(작업자) 단일 조회
    public UserResponse getUser(Long id) throws NotFoundException {
        User user = getUserOrThrow(id);
        return mapper.toResponse(user, UserResponse.class);
    }

    // 직원(작업자) 페이징 조회
    public Page<UserResponse> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(users, UserResponse.class);
    }

    // 직원(작업자) 수정
    public UserResponse updateUser(Long id, UserRequest userRequest) throws NotFoundException {
        Department newDepartment = departmentService.getDepartmentOrThrow(userRequest.getDepartmentId());
        User newEmp = mapper.toEntity(userRequest, User.class);
        User findEmp = getUserOrThrow(id);
        findEmp.put(newEmp, newDepartment);
        userRepository.save(findEmp);
        return mapper.toResponse(findEmp, UserResponse.class);
    }

    // 직원(작업자) 삭제
    public void deleteUser(Long id) throws NotFoundException {
        User user = getUserOrThrow(id);
        user.delete();
        userRepository.save(user);
    }

    // 로그인
    @Override
    public UserResponse getLogin(String userCode, String password) throws NotFoundException {
        User user = userRepository.findByUserCodeAndPassword(userCode, password);
        if (user == null) {
            throw new NotFoundException("user does not exists. input userCode: " + userCode + ", password: " + password);
        }
        return mapper.toResponse(user, UserResponse.class);
    }
}
