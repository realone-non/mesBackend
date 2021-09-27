package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.repository.BusinessTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BusinessTypeServiceImpl implements BusinessTypeService {

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    // 업태 타입 생성
    public BusinessType createBusinessType(BusinessType businessType) {
        return businessTypeRepository.save(businessType);
    }

    // 업태 타입 조회
    public BusinessType getBusinessType(Long id) {
        return businessTypeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    // 업태 타입 리스트 조회
    public Page<BusinessType> getBusinessTypes(Pageable pageable) {
        return businessTypeRepository.findAll(pageable);
    }

    // 업태 타입 수정
    public BusinessType updateBusinessTypes(Long id, BusinessType businessType) {
        BusinessType findBusinessType = getBusinessType(id);
        findBusinessType.setName(businessType.getName());
        return businessTypeRepository.save(findBusinessType);
    }

    // 업태 삭제
    public void deleteBusinessType(Long id) {
        businessTypeRepository.deleteById(id);
    }
}
