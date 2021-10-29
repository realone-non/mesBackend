package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByIdAndDeleteYnFalse(Long id);
    Page<User> findAllByDeleteYnFalse(Pageable pageable);
    User findByUserCodeAndPassword(String userCode, String password);

//    boolean existsByUserCodeAndDeleteYnFalse(String userCode);
//    boolean existsByPasswordAndDeleteYnFalse(String password);
}