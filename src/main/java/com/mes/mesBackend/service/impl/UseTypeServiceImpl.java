package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.UseTypeRequest;
import com.mes.mesBackend.dto.response.UseTypeResponse;
import com.mes.mesBackend.entity.UseType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.UseTypeRepository;
import com.mes.mesBackend.service.UseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 용도유형
@Service
public class UseTypeServiceImpl implements UseTypeService {
    @Autowired
    UseTypeRepository useTypeRepository;
    @Autowired
    ModelMapper mapper;

    // 용도유형 생성
    @Override
    public UseTypeResponse createUseType(UseTypeRequest useTypeRequest) {
        UseType useType = mapper.toEntity(useTypeRequest, UseType.class);
        useTypeRepository.save(useType);
        return mapper.toResponse(useType, UseTypeResponse.class);
    }

    // 용도유형 단일 조회
    @Override
    public UseTypeResponse getUseType(Long id) throws NotFoundException {
        UseType useType = getUseTypeOrThrow(id);
        return mapper.toResponse(useType, UseTypeResponse.class);
    }

    // 용도유형 리스트 조회
    @Override
    public List<UseTypeResponse> getUseTypes() {
        List<UseType> useTypes = useTypeRepository.findAllByDeleteYnFalse();
        return mapper.toListResponses(useTypes, UseTypeResponse.class);
    }

    // 용도유형 수정
    @Override
    public UseTypeResponse updateUseType(Long id, UseTypeRequest useTypeRequest) throws NotFoundException {
        UseType findUseType = getUseTypeOrThrow(id);
        UseType newUseType = mapper.toEntity(useTypeRequest, UseType.class);
        findUseType.put(newUseType);
        useTypeRepository.save(findUseType);
        return mapper.toResponse(findUseType, UseTypeResponse.class);
    }

    // 용도유형 삭제
    @Override
    public void deleteUseType(Long id) throws NotFoundException {
        UseType useType = getUseTypeOrThrow(id);
        useType.delete();
        useTypeRepository.save(useType);
    }

    // 용도유형 조회 및 예외
    @Override
    public UseType getUseTypeOrThrow(Long id) throws NotFoundException {
        return useTypeRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("useType does not exist. input id: " + id));
    }
}
