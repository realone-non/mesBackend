package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.entity.UserVo;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.UserRepository;
import com.mes.mesBackend.repository.UserVoRepository;
import com.mes.mesBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserVoRepository userVoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper mapper;

    public Page<UserVo> getUsers(Pageable pageable) {
//        Page<UserVo> users = userRepository.findAll(pageable);
        return userVoRepository.findAllByDeleteYnFalse(pageable);
    }

    public UserVo getUser(Long id){
        return userVoRepository.findByIdAndDeleteYnFalse(id);
    }

    public UserVo findByNickNameAndPassword(String nickName, String password) {
        return userVoRepository.findByNickNameAndPasswordAndDeleteYnFalse(nickName, password);
    }

    public UserVo updateUser(Long id, UserVo updateUser){
        UserVo user = getUser(id);
        user.setUserName(updateUser.getUserName());
        return userVoRepository.save(user);
    }

    public void deleteUser(Long id) {
        UserVo user = getUser(id);
        user.setDeleteYn(true);
        userVoRepository.save(user);
    }

    public UserVo createUser(UserVo user){
        return userVoRepository.save(user);
    }

    @Override
    public User getUserOrThrow(Long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("user does not exist. input id: " + id));
    }
}
