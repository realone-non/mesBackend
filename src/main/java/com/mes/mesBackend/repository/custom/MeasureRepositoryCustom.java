package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.Measure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MeasureRepositoryCustom {
    // 계측기 전체 조회 검색조건: 검색조건: GAUGE유형, 검교정 주기
    List<Measure> findAllByCondition(Long gaugeTypeId, Integer calibrationCycle);

    // 계측기 페이징 조회 검색조건: 검색조건: GAUGE유형, 검교정대상(월)
//    Page<Measure> findAllByCondition(Long gaugeTypeId, Long month, Pageable pageable);
}
