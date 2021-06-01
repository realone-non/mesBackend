package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.UserVo;
import com.mes.mesBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    public List<UserVo> FindAll(){
        return (List<UserVo>) userRepository.findAll();
    }

    public Optional<UserVo> Find(String id){
        return userRepository.findByUserId(id);
    }

    public Optional<UserVo> Find(String id, String password){
        return userRepository.findByUserIdAndPassword(id, password);
    }

    public void Update(UserVo user){
        Optional<UserVo> e = userRepository.findById(user.getUserId());

        e.ifPresent(selectUser->{
            selectUser.setDescription(user.getDescription());
            selectUser.setFactoryCode(user.getFactoryCode());
            selectUser.setEmail(user.getEmail());
            selectUser.setUserName(user.getUserName());
            selectUser.setPassword(user.getPassword());
            selectUser.setTelephone(user.getTelephone());
            selectUser.setModifiedDate(new Date(System.currentTimeMillis()));
            selectUser.setModifyId(user.getUserId());
            userRepository.save(selectUser);
        });
    }

    public void Delete(String id){
        userRepository.deleteById(id);
    }

    public UserVo Save(UserVo user){
        return userRepository.save(user);
    }
}
