package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.WareHouseTypeRequest;
import com.mes.mesBackend.dto.response.WareHouseTypeResponse;
import com.mes.mesBackend.entity.WareHouseType;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// 창고유형
public interface WareHouseTypeService {
    // 생성
    WareHouseTypeResponse createWareHouseType(WareHouseTypeRequest wareHouseTypeRequest);

    // 단일조회
    WareHouseTypeResponse getWareHouseType(Long id) throws NotFoundException;

    // 페이징조회
    Page<WareHouseTypeResponse> getWareHouseTypes(Pageable pageable);

    // 수정
    WareHouseTypeResponse updateWareHouseType(Long id, WareHouseTypeRequest wareHouseTypeRequest) throws NotFoundException;

    // 삭제
    void deleteWareHouseType(Long id) throws NotFoundException;

    WareHouseType getWareHouseTypeOrThrow(Long id) throws NotFoundException;
}
