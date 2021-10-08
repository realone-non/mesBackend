package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.UserVo;
import com.mes.mesBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    public Page<UserVo> getUsers(Pageable pageable) {
//        Page<UserVo> users = userRepository.findAll(pageable);
        return userRepository.findAllByDeleteYnFalse(pageable);
    }

    public UserVo getUser(Long id){
        return userRepository.findByIdAndDeleteYnFalse(id);
    }

    public UserVo findByNickNameAndPassword(String nickName, String password) {
        return userRepository.findByNickNameAndPasswordAndDeleteYnFalse(nickName, password);
    }

    public UserVo updateUser(Long id, UserVo updateUser){
        UserVo user = getUser(id);
        user.setUserName(updateUser.getUserName());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        UserVo user = getUser(id);
        user.setDeleteYn(true);
        userRepository.save(user);
    }

    public UserVo createUser(UserVo user){
        return userRepository.save(user);
    }
}
