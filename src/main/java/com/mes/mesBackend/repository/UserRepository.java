package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaCustomRepository<User, Long> {
    User findByUserCodeAndPassword(String userCode, String password);
}