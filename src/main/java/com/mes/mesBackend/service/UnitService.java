package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.UnitRequest;
import com.mes.mesBackend.dto.response.UnitResponse;
import com.mes.mesBackend.entity.Unit;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// 단위
public interface UnitService {
    // 생성
    UnitResponse createUnit(UnitRequest unitRequest) throws NotFoundException, BadRequestException;

    // 단일 조회
    UnitResponse getUnit(Long id) throws NotFoundException;

    // 페이징 조회
    Page<UnitResponse> getUnits(Pageable pageable);

    // 수정
    UnitResponse updateUnit(Long id, UnitRequest unitRequest) throws NotFoundException;

    // 삭제
    void deleteUnit(Long id) throws NotFoundException;

    // 예외처리 단일조회
    Unit findUnitOrThrow(Long id) throws NotFoundException;

}
