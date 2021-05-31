package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.UserVo;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserVo> FindAll();
    Optional<UserVo> Find(String id);
    Optional<UserVo> Find(String id, String password);
    void Update(UserVo user);
    void Delete(String id);
    UserVo Save(UserVo user);
}