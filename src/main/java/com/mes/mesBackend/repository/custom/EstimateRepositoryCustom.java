package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.Estimate;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface EstimateRepositoryCustom {
    // 견적 페이징 조회 검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 담당자
    Page<Estimate> findAllByCondition(String clientName, LocalDateTime startDate, LocalDateTime endDate, Long currencyId, String chargeName);
}
