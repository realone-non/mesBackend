package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.UserVo;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Page<UserVo> getUsers(Pageable pageable);
    UserVo getUser(Long id);
    UserVo findByNickNameAndPassword(String nickName, String password);
    UserVo updateUser(Long id, UserVo user);
    void deleteUser(Long id);
    UserVo createUser(UserVo user);

    User getUserOrThrow(Long id) throws NotFoundException;
}