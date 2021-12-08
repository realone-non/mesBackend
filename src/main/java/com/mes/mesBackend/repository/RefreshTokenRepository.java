package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    List<RefreshToken> findAllByUserCodeAndUseYnTrue(String userCode);
    Optional<RefreshToken> findByUserCodeAndUseYnTrue(String userCode);
}