package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.BusinessTypeRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BusinessTypeService {

    // 업태 타입 생성
    BusinessTypeResponse createBusinessType(BusinessTypeRequest businessTypeRequest);

    // 업태 타입 조회
    BusinessTypeResponse getBusinessType(Long id) throws NotFoundException;

    // 업태 타입 전체 조회
    Page<BusinessTypeResponse> getBusinessTypes(Pageable pageable);

    // 업태 타입 수정
    BusinessTypeResponse updateBusinessType(Long id, BusinessTypeRequest businessTypeRequest) throws NotFoundException;

    // 업태 삭제
    void deleteBusinessType(Long id) throws NotFoundException;

    BusinessType getBusinessTypeOrThrow(Long id) throws NotFoundException;
}
