package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.CheckTypeRequest;
import com.mes.mesBackend.dto.response.CheckTypeResponse;
import com.mes.mesBackend.entity.CheckType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.CheckTypeRepository;
import com.mes.mesBackend.service.CheckTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckTypeServiceImpl implements CheckTypeService {
    @Autowired
    CheckTypeRepository checkTypeRepository;
    @Autowired
    ModelMapper mapper;

    // 점검유형 생성
    @Override
    public CheckTypeResponse createCheckType(CheckTypeRequest checkTypeRequest) {
        CheckType checkType = mapper.toEntity(checkTypeRequest, CheckType.class);
        checkTypeRepository.save(checkType);
        return mapper.toResponse(checkType, CheckTypeResponse.class);
    }

    // 점검유형 단일조회
    @Override
    public CheckTypeResponse getCheckType(Long id) throws NotFoundException {
        CheckType checkType = getCheckTypeOrThrow(id);
        return mapper.toResponse(checkType, CheckTypeResponse.class);
    }

    // 점검유형 조회 및 예외
    @Override
    public CheckType getCheckTypeOrThrow(Long id) throws NotFoundException {
        return checkTypeRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("checkType does not exist. input id: " + id));
    }

    // 점검유형 전체 조회
    @Override
    public List<CheckTypeResponse> getCheckTypes() {
        List<CheckType> checkTypes = checkTypeRepository.findAllByDeleteYnFalseOrderByCreatedDateDesc();
        return mapper.toListResponses(checkTypes, CheckTypeResponse.class);
    }

    // 점검유형 수정
    @Override
    public CheckTypeResponse updateCheckType(Long id, CheckTypeRequest checkTypeRequest) throws NotFoundException {
        CheckType findCheckType = getCheckTypeOrThrow(id);
        CheckType newCheckType = mapper.toEntity(checkTypeRequest, CheckType.class);
        findCheckType.put(newCheckType);
        checkTypeRepository.save(findCheckType);
        return mapper.toResponse(findCheckType, CheckTypeResponse.class);
    }

    // 점검유형 삭제
    @Override
    public void deleteCheckType(Long id) throws NotFoundException {
        CheckType checkType = getCheckTypeOrThrow(id);
        checkType.delete();
        checkTypeRepository.save(checkType);
    }
}
