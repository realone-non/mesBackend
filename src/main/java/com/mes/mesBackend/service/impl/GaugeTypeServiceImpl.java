package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.GaugeTypeRequest;
import com.mes.mesBackend.dto.response.GaugeTypeResponse;
import com.mes.mesBackend.entity.GaugeType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.GaugeTypeRepository;
import com.mes.mesBackend.service.GaugeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GaugeTypeServiceImpl implements GaugeTypeService {
    @Autowired
    GaugeTypeRepository gaugeTypeRepository;

    @Autowired
    ModelMapper modelMapper;

    // GAUGE 타입 생성
    public GaugeTypeResponse createGaugeType(GaugeTypeRequest gaugeTypeRequest) {
        GaugeType gaugeType = modelMapper.toEntity(gaugeTypeRequest, GaugeType.class);
        gaugeTypeRepository.save(gaugeType);
        return modelMapper.toResponse(gaugeType, GaugeTypeResponse.class);
    }

    // GAUGE 타입 조회
    public GaugeTypeResponse getGaugeType(Long id) throws NotFoundException {
        GaugeType gaugeType = getGaugeTypeOrThrow(id);
        return modelMapper.toResponse(gaugeType, GaugeTypeResponse.class);
    }

    // Gauge 유형 전체 조회
    public List<GaugeTypeResponse> getGaugeTypes() {
        List<GaugeType> gaugeTypes = gaugeTypeRepository.findAllByDeleteYnFalseOrderByCreatedDateDesc();
        return modelMapper.toListResponses(gaugeTypes, GaugeTypeResponse.class);
    }

    // GAUGE 타입 수정
    public GaugeTypeResponse updateGaugeType(Long id, GaugeTypeRequest gaugeTypeRequest) throws NotFoundException {
        GaugeType newGaugeType = modelMapper.toEntity(gaugeTypeRequest, GaugeType.class);
        GaugeType findGaugeType = getGaugeTypeOrThrow(id);
        findGaugeType.update(newGaugeType);
        gaugeTypeRepository.save(findGaugeType);
        return modelMapper.toResponse(findGaugeType, GaugeTypeResponse.class);
    }

    // GAUGE 타입 삭제
    public void deleteGaugeType(Long id) throws NotFoundException {
        GaugeType gaugeType = getGaugeTypeOrThrow(id);
        gaugeType.delete();
        gaugeTypeRepository.save(gaugeType);
    }
    // Gauge 유형 단일 조회 및 예외
    public GaugeType getGaugeTypeOrThrow(Long id) throws NotFoundException {
        return gaugeTypeRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("gauge type does not exist. input id: " + id));
    }
}
