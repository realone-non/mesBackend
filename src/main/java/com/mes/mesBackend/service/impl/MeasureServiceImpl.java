package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.MeasureRequest;
import com.mes.mesBackend.dto.response.MeasureResponse;
import com.mes.mesBackend.entity.Department;
import com.mes.mesBackend.entity.GaugeType;
import com.mes.mesBackend.entity.Measure;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.MeasureRepository;
import com.mes.mesBackend.service.DepartmentService;
import com.mes.mesBackend.service.GaugeTypeService;
import com.mes.mesBackend.service.MeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MeasureServiceImpl implements MeasureService {

    @Autowired
    MeasureRepository measureRepository;
    @Autowired
    GaugeTypeService gaugeTypeService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    ModelMapper mapper;

    // 계측기 생성
    @Override
    public MeasureResponse createMeasure(MeasureRequest measureRequest) throws NotFoundException {
        GaugeType gaugeType = gaugeTypeService.getGaugeTypeOrThrow(measureRequest.getGaugeType());
        Department department = departmentService.getDepartmentOrThrow(measureRequest.getDepartment());
        Measure measure = mapper.toEntity(measureRequest, Measure.class);
        measure.addMapping(gaugeType, department, measure.getCalibrationLastDate(), measure.getCalibrationCycle());
        measureRepository.save(measure);
        return mapper.toResponse(measure, MeasureResponse.class);
    }

    // 계측기 단일 조회
    @Override
    public MeasureResponse getMeasure(Long id) throws NotFoundException {
        Measure measure = getMeasureOrThrow(id);
        return mapper.toResponse(measure, MeasureResponse.class);
    }

    // 계측기 페이징 조회 검색조건: 검색조건: GAUGE유형, 검교정대상(월)
    @Override
    public Page<MeasureResponse> getMeasures(Long gaugeId, Long month, Pageable pageable) {
        Page<Measure> measures = measureRepository.findAllByCondition(gaugeId, month, pageable);
        return mapper.toPageResponses(measures, MeasureResponse.class);
    }

    // 계측기 수정
    @Override
    public MeasureResponse updateMeasure(Long id, MeasureRequest measureRequest) throws NotFoundException {
        Measure findMeasure = getMeasureOrThrow(id);
        GaugeType newGaugeType = gaugeTypeService.getGaugeTypeOrThrow(measureRequest.getGaugeType());
        Department newDepartment = departmentService.getDepartmentOrThrow(measureRequest.getDepartment());
        Measure newMeasure = mapper.toEntity(measureRequest, Measure.class);
        findMeasure.update(newMeasure, newGaugeType, newDepartment);
        measureRepository.save(findMeasure);
        return mapper.toResponse(findMeasure, MeasureResponse.class);
    }

    // 계측기 삭제
    @Override
    public void deleteMeasure(Long id) throws NotFoundException {
        Measure measure = getMeasureOrThrow(id);
        measure.delete();
        measureRepository.save(measure);
    }

    // 계측기 단일 조회 및 예외
    private Measure getMeasureOrThrow(Long id) throws NotFoundException {
        return measureRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("measure does not exist. input id: " + id));
    }
}
