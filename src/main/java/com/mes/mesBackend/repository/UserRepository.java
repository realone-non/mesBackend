package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.User;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.UserRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaCustomRepository<User, Long>, UserRepositoryCustom {
    User findByUserCodeAndPassword(String userCode, String password);
    Optional<User> findByUserCode(String userCode);
    boolean existsByUserCode(String userCode);
}