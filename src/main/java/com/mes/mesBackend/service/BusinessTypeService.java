package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.BusinessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BusinessTypeService {

    // 업태 타입 생성
    BusinessType createBusinessType(BusinessType businessType);

    // 업태 타입 조회
    BusinessType getBusinessType(Long id);

    // 업태 타입 리스트 조회
    Page<BusinessType> getBusinessTypes(Pageable pageable);

    // 업태 타입 수정
    BusinessType updateBusinessTypes(Long id, BusinessType businessType);

    // 업태 삭제
    void deleteBusinessType(Long id);
}
