package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.BusinessTypeRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.BusinessTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessTypeServiceImpl implements BusinessTypeService {

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    @Autowired Mapper mapper;

    public BusinessType findBusinessTypeByIdAndDeleteYn(Long id) {
        return businessTypeRepository.findByIdAndDeleteYnFalse(id);
    }

    // 업태 타입 생성
    public BusinessTypeResponse createBusinessType(BusinessTypeRequest businessTypeRequest) {
        BusinessType businessType = mapper.toEntity(businessTypeRequest, BusinessType.class);
        BusinessType saveBusinessType = businessTypeRepository.save(businessType);
        return mapper.toResponse(saveBusinessType, BusinessTypeResponse.class);
    }

    // 업태 타입 조회
    public BusinessTypeResponse getBusinessType(Long id) {
        BusinessType businessType = findBusinessTypeByIdAndDeleteYn(id);
        return mapper.toResponse(businessType, BusinessTypeResponse.class);
    }

    // 업체 타입 전체 조회
    public Page<BusinessTypeResponse> getBusinessTypes(Pageable pageable) {
        Page<BusinessType> businessTypes = businessTypeRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(businessTypes, BusinessTypeResponse.class);
    }

    // 업태 타입 수정
    public BusinessTypeResponse updateBusinessType(Long id, BusinessTypeRequest businessTypeRequest) {
        BusinessType businessType = mapper.toEntity(businessTypeRequest, BusinessType.class);
        BusinessType findBusinessType = findBusinessTypeByIdAndDeleteYn(id);
        findBusinessType.setName(businessType.getName());
        BusinessType updateBusinessType = businessTypeRepository.save(findBusinessType);
        return mapper.toResponse(updateBusinessType, BusinessTypeResponse.class);
    }

    // 업태 삭제
    public void deleteBusinessType(Long id) {
        BusinessType businessType = findBusinessTypeByIdAndDeleteYn(id);
        businessType.setDeleteYn(true);
        businessTypeRepository.save(businessType);
    }
}
