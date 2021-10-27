package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.BusinessTypeRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.BusinessTypeRepository;
import com.mes.mesBackend.service.BusinessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BusinessTypeServiceImpl implements BusinessTypeService {

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    @Autowired Mapper mapper;

    // 삭제여부 false, 사용여부 true,false
    public BusinessType findBusinessTypeByIdAndDeleteYn(Long id) throws NotFoundException {
        BusinessType findBusinessType = businessTypeRepository.findByIdAndDeleteYnFalse(id);
        if (findBusinessType == null) {
            throw new NotFoundException("business type does not exist. input businessTypeId: " + id);
        }
        return findBusinessType;
    }

    // 업태 타입 생성
    public BusinessTypeResponse createBusinessType(BusinessTypeRequest businessTypeRequest) {
        BusinessType businessType = mapper.toEntity(businessTypeRequest, BusinessType.class);
        BusinessType saveBusinessType = businessTypeRepository.save(businessType);
        return mapper.toResponse(saveBusinessType, BusinessTypeResponse.class);
    }

    // 업태 타입 조회
    public BusinessTypeResponse getBusinessType(Long id) throws NotFoundException {
        BusinessType businessType = findBusinessTypeByIdAndDeleteYn(id);
        return mapper.toResponse(businessType, BusinessTypeResponse.class);
    }

    // 업체 타입 전체 조회
    public Page<BusinessTypeResponse> getBusinessTypes(Pageable pageable) {
        Page<BusinessType> businessTypes = businessTypeRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(businessTypes, BusinessTypeResponse.class);
    }

    // 업태 타입 수정
    public BusinessTypeResponse updateBusinessType(Long id, BusinessTypeRequest businessTypeRequest) throws NotFoundException {
        BusinessType businessType = mapper.toEntity(businessTypeRequest, BusinessType.class);
        BusinessType findBusinessType = findBusinessTypeByIdAndDeleteYn(id);
        findBusinessType.setName(businessType.getName());
        BusinessType updateBusinessType = businessTypeRepository.save(findBusinessType);
        return mapper.toResponse(updateBusinessType, BusinessTypeResponse.class);
    }

    // 업태 삭제
    public void deleteBusinessType(Long id) throws NotFoundException {
        BusinessType businessType = findBusinessTypeByIdAndDeleteYn(id);
        businessType.setDeleteYn(true);
        businessTypeRepository.save(businessType);
    }
}
