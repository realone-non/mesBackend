package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.BusinessTypeRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.entity.BusinessType;

import java.util.List;

public interface BusinessTypeService {

    // 업태 타입 생성
    BusinessTypeResponse createBusinessType(BusinessTypeRequest businessTypeRequest);

    // 업태 타입 조회
    BusinessTypeResponse getBusinessType(Long id);

    // 업태 타입 리스트 조회
    List<BusinessTypeResponse> getBusinessTypes();

    // 업태 타입 수정
    BusinessTypeResponse updateBusinessType(Long id, BusinessTypeRequest businessTypeRequest);

    // 업태 삭제
    void deleteBusinessType(Long id);

    // 업태 조회 Entity 반환
    BusinessType findBusinessType(Long id);
}
