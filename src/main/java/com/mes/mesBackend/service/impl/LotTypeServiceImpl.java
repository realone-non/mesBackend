package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.LotTypeRequest;
import com.mes.mesBackend.dto.response.LotTypeResponse;
import com.mes.mesBackend.entity.LotType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.LotTypeRepository;
import com.mes.mesBackend.service.LotTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LotTypeServiceImpl implements LotTypeService {
    private final LotTypeRepository lotTypeRepository;
    private final ModelMapper mapper;

    // Lot유형 생성
    @Override
    public LotTypeResponse createLotType(LotTypeRequest lotTypeRequest) {
        LotType lotType = mapper.toEntity(lotTypeRequest, LotType.class);
        lotTypeRepository.save(lotType);
        return mapper.toResponse(lotType, LotTypeResponse.class);
    }

    // Lot유형 단일 조회
    @Override
    public LotTypeResponse getLotType(Long id) throws NotFoundException {
        LotType lotType = getLotTypeOrThrow(id);
        return mapper.toResponse(lotType, LotTypeResponse.class);
    }

    // Lot유형 리스트 조회
    @Override
    public List<LotTypeResponse> getLotTypes() {
        List<LotType> lotTypes = lotTypeRepository.findAllByDeleteYnFalse();
        return mapper.toListResponses(lotTypes, LotTypeResponse.class);
    }

    // Lot유형 수정
    @Override
    public LotTypeResponse updateLotType(Long id, LotTypeRequest lotTypeRequest) throws NotFoundException {
        LotType findLotType = getLotTypeOrThrow(id);
        LotType newLotType = mapper.toEntity(lotTypeRequest, LotType.class);
        findLotType.put(newLotType);
        lotTypeRepository.save(findLotType);
        return mapper.toResponse(findLotType, LotTypeResponse.class);
    }

    // Lot유형 삭제
    @Override
    public void deleteLotType(Long id) throws NotFoundException {
        LotType lotType = getLotTypeOrThrow(id);
        lotType.delete();
        lotTypeRepository.save(lotType);
    }

    // Lot유형 조회 및 예외
    @Override
    public LotType getLotTypeOrThrow(Long id) throws NotFoundException {
        return lotTypeRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("lotType does not exist. input id: " + id));
    }
}
