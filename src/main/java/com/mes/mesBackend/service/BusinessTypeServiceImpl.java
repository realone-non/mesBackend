package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.BusinessTypeRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.entity.BusinessType;
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

    @Autowired
    ModelMapper modelMapper;

    public BusinessType findBusinessTypeByIdAndUseYn(Long id) {
        return businessTypeRepository.findByIdAndUseYnTrue(id);
    }

    // 업태 타입 생성
    public BusinessTypeResponse createBusinessType(BusinessTypeRequest businessTypeRequest) {
        BusinessType businessType = businessRequestToBusiness(businessTypeRequest);
        BusinessType saveBusinessType = businessTypeRepository.save(businessType);
        return businessToBusinessResponse(saveBusinessType);
    }

    // 업태 타입 조회
    public BusinessTypeResponse getBusinessType(Long id) {
        BusinessType businessType = findBusinessTypeByIdAndUseYn(id);
        return businessToBusinessResponse(businessType);
    }

    // 업체 타입 전체 조회
    public Page<BusinessTypeResponse> getBusinessTypes(Pageable pageable) {
        Page<BusinessType> businessTypes = businessTypeRepository.findAllByUseYnTrue(pageable);
        return businessTypeToPageBusinessTypeResponses(businessTypes);
    }

    // 업태 타입 수정
    public BusinessTypeResponse updateBusinessType(Long id, BusinessTypeRequest businessTypeRequest) {
        BusinessType businessType = businessRequestToBusiness(businessTypeRequest);
        BusinessType findBusinessType = findBusinessTypeByIdAndUseYn(id);
        findBusinessType.setName(businessType.getName());
        BusinessType updateBusinessType = businessTypeRepository.save(findBusinessType);
        return businessToBusinessResponse(updateBusinessType);
    }

    // 업태 삭제
    public void deleteBusinessType(Long id) {
        BusinessType businessType = findBusinessTypeByIdAndUseYn(id);
        businessType.setUseYn(false);
        businessTypeRepository.save(businessType);
    }

    // Entity -> Response
    private BusinessTypeResponse businessToBusinessResponse(BusinessType businessType) {
        return modelMapper.map(businessType, BusinessTypeResponse.class);
    }

    // List<entity> -> List<Response>
    private List<BusinessTypeResponse> businessToListBusinessResponse(List<BusinessType> businessTypes) {
        return businessTypes
                .stream()
                .map(businessType ->
                modelMapper.map(businessType, BusinessTypeResponse.class)).collect(Collectors.toList());
    }

    // Request -> Entity
    private BusinessType businessRequestToBusiness(BusinessTypeRequest businessTypeRequest) {
        return modelMapper.map(businessTypeRequest, BusinessType.class);
    }

    // Page<Entity> -> Page<Response>
    private Page<BusinessTypeResponse> businessTypeToPageBusinessTypeResponses(Page<BusinessType> businessTypes) {
        return businessTypes.map(businessType -> modelMapper.map(businessType, BusinessTypeResponse.class));
    }
}
