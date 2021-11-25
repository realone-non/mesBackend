package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.RefreshToken;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserCode(String userCode);
    @Transactional
    void deleteByUserCode(String userCode);
}