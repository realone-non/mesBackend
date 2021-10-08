package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.UserVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserVo, Long> {
    UserVo findByNickNameAndPasswordAndDeleteYnFalse(String nickName, String password);
    Page<UserVo> findAllByDeleteYnFalse(Pageable pageable);
    UserVo findByIdAndDeleteYnFalse(Long Id);
}
