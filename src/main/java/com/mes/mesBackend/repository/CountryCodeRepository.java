package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.CountryCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryCodeRepository extends JpaRepository<CountryCode, Long> {
    CountryCode findByIdAndDeleteYnFalse(Long id);
    Page<CountryCode> findAllByDeleteYnFalse(Pageable pageable);
}