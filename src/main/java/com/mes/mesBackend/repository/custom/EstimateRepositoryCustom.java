package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.Estimate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EstimateRepositoryCustom {
    // 견적 페이징 조회 검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 담당자
    Page<Estimate> findAllByCondition(String clientName, LocalDate startDate, LocalDate endDate, Long currencyId, String chargeName, Pageable pageable);
}
