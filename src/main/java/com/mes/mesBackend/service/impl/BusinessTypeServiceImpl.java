package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.BusinessTypeRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.BusinessTypeRepository;
import com.mes.mesBackend.service.BusinessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
public class BusinessTypeServiceImpl implements BusinessTypeService {

    @Autowired
    BusinessTypeRepository businessTypeRepository;

    @Autowired
    ModelMapper modelMapper;

    // 삭제여부 false, 사용여부 true,false
    public BusinessType getBusinessTypeOrThrow(Long id) throws NotFoundException {
        return businessTypeRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("business type does not exist. input businessTypeId: " + id));
    }

    // 업태 타입 생성
    public BusinessTypeResponse createBusinessType(BusinessTypeRequest businessTypeRequest) {
        BusinessType businessType = modelMapper.toEntity(businessTypeRequest, BusinessType.class);
        businessTypeRepository.save(businessType);
        return modelMapper.toResponse(businessType, BusinessTypeResponse.class);
    }

    // 업태 타입 조회
    public BusinessTypeResponse getBusinessType(Long id) throws NotFoundException {
        BusinessType businessType = getBusinessTypeOrThrow(id);
        return modelMapper.toResponse(businessType, BusinessTypeResponse.class);
    }

    // 업체 타입 전체 조회
    public List<BusinessTypeResponse> getBusinessTypes() {
        List<BusinessType> businessTypes = businessTypeRepository.findAllByDeleteYnFalse();
        return modelMapper.toListResponses(businessTypes, BusinessTypeResponse.class);
    }
//    public Page<BusinessTypeResponse> getBusinessTypes(Pageable pageable) {
//        Page<BusinessType> businessTypes = businessTypeRepository.findAllByDeleteYnFalse(pageable);
//        return modelMapper.toPageResponses(businessTypes, BusinessTypeResponse.class);
//    }

    // 업태 타입 수정
    public BusinessTypeResponse updateBusinessType(Long id, BusinessTypeRequest businessTypeRequest) throws NotFoundException {
        BusinessType newBusinessType = modelMapper.toEntity(businessTypeRequest, BusinessType.class);
        BusinessType findBusinessType = getBusinessTypeOrThrow(id);
        findBusinessType.put(newBusinessType);
        businessTypeRepository.save(findBusinessType);
        return modelMapper.toResponse(findBusinessType, BusinessTypeResponse.class);
    }

    // 업태 삭제
    public void deleteBusinessType(Long id) throws NotFoundException {
        BusinessType businessType = getBusinessTypeOrThrow(id);
        businessType.delete();
        businessTypeRepository.save(businessType);
    }
}
