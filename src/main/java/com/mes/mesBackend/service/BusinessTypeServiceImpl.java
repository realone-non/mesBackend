package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.BusinessTypeRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.repository.BusinessTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessTypeServiceImpl implements BusinessTypeService {

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    @Autowired
    ModelMapper modelMapper;

    public BusinessType findBusinessType(Long id) {
        return businessTypeRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("no such data"));
    }

    // 업태 타입 생성
    public BusinessTypeResponse createBusinessType(BusinessTypeRequest businessTypeRequest) {
        BusinessType businessType = businessRequestToBusiness(businessTypeRequest);
        BusinessType saveBusinessType = businessTypeRepository.save(businessType);
        return businessToBusinessResponse(saveBusinessType);
    }

    // 업태 타입 조회
    public BusinessTypeResponse getBusinessType(Long id) {
        BusinessType businessType = findBusinessType(id);
        return businessToBusinessResponse(businessType);
    }

    // 업태 타입 리스트 조회
    public List<BusinessTypeResponse> getBusinessTypes() {
        List<BusinessType> businessTypes = businessTypeRepository.findAll();
        return businessToListBusinessResponse(businessTypes);
    }

    // 업태 타입 수정
    public BusinessTypeResponse updateBusinessType(Long id, BusinessTypeRequest businessTypeRequest) {
        BusinessType businessType = businessRequestToBusiness(businessTypeRequest);
        BusinessType findBusinessType = findBusinessType(id);
        findBusinessType.setName(businessType.getName());
        BusinessType updateBusinessType = businessTypeRepository.save(findBusinessType);
        return businessToBusinessResponse(updateBusinessType);
    }

    // 업태 삭제
    public void deleteBusinessType(Long id) {
        businessTypeRepository.deleteById(id);
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
}
