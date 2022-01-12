package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.MeasureCalibrationRequest;
import com.mes.mesBackend.dto.response.MeasureCalibrationResponse;
import com.mes.mesBackend.entity.Department;
import com.mes.mesBackend.entity.Measure;
import com.mes.mesBackend.entity.MeasureCalibration;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.S3Uploader;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.MeasureCalibrationRepository;
import com.mes.mesBackend.service.DepartmentService;
import com.mes.mesBackend.service.MeasureCalibrationService;
import com.mes.mesBackend.service.MeasureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

// 17-5. 계측기 검교정 실적 등록
@Service
@RequiredArgsConstructor
public class MeasureCalibrationServiceImpl implements MeasureCalibrationService {
    private final ModelMapper mapper;
    private final MeasureCalibrationRepository measureCalibrationRepo;
    private final MeasureService measureService;
    private final DepartmentService departmentService;
    private final S3Uploader s3Uploader;

    // 계측기 검교정 실적 생성
    @Override
    public MeasureCalibrationResponse createMeasureCalibration(MeasureCalibrationRequest measureCalibrationRequest) throws NotFoundException {
        Measure measure = measureService.getMeasureOrThrow(measureCalibrationRequest.getMeasureId());
        Department department = departmentService.getDepartmentOrThrow(measureCalibrationRequest.getRequestDepartmentId());
        MeasureCalibration measureCalibration = mapper.toEntity(measureCalibrationRequest, MeasureCalibration.class);
        measureCalibration.add(measure, department);
        measureCalibrationRepo.save(measureCalibration);
        return getMeasureCalibrationResponse(measureCalibration.getId());
    }

    // 계측기 검교정 실적 단일 조회
    @Override
    public MeasureCalibrationResponse getMeasureCalibrationResponse(Long id) throws NotFoundException {
        return measureCalibrationRepo.findMeasureCalibrationResponseById(id)
                .orElseThrow(() -> new NotFoundException("measureCalibration does not exist. input id: " + id));
    }

    // 계측기 검교정 실적 리스트 검색 조회, 검색조건: 검정처(부서 id), 측정기유형(계측기유형), 검정기간(검교정일자) fromDate~toDate
    @Override
    public List<MeasureCalibrationResponse> getMeasureCalibrations(Long departmentId, Long gaugeTypeId, LocalDate fromDate, LocalDate toDate) {
        return measureCalibrationRepo.findMeasureCalibrationResponsesByCondition(departmentId, gaugeTypeId, fromDate, toDate);
    }

    // 계측기 검교정 실적 수정
    @Override
    public MeasureCalibrationResponse updateMeasureCalibration(Long id, MeasureCalibrationRequest measureCalibrationRequest) throws NotFoundException {
        MeasureCalibration findMeasureCalibration = getMeasureCalibrationOrThrow(id);
        Measure newMeasure = measureService.getMeasureOrThrow(measureCalibrationRequest.getMeasureId());
        Department newDepartment = departmentService.getDepartmentOrThrow(measureCalibrationRequest.getRequestDepartmentId());
        MeasureCalibration newMeasureCalibration = mapper.toEntity(measureCalibrationRequest, MeasureCalibration.class);

        findMeasureCalibration.update(newMeasureCalibration, newMeasure, newDepartment);

        measureCalibrationRepo.save(findMeasureCalibration);
        return getMeasureCalibrationResponse(findMeasureCalibration.getId());
    }

    // 계측기 검교정 실적 삭제
    @Override
    public void deleteMeasureCalibration(Long id) throws NotFoundException {
        MeasureCalibration measureCalibration = getMeasureCalibrationOrThrow(id);
        measureCalibration.delete();
        measureCalibrationRepo.save(measureCalibration);
    }

    private MeasureCalibration getMeasureCalibrationOrThrow(Long id) throws NotFoundException {
        return measureCalibrationRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("measureCalibration does not exist. input id: " + id));
    }

    // 계측기 검교정 성적서 파일 생성
    @Override
    public MeasureCalibrationResponse createFilesToMeasureCalibration(Long id, MultipartFile file) throws NotFoundException, BadRequestException, IOException {
        MeasureCalibration measureCalibration = getMeasureCalibrationOrThrow(id);
        String fileName = "measure-calibration/" + id + "/";
        String uploadFileName = s3Uploader.upload(file, fileName);
        measureCalibration.setReportFileUrl(uploadFileName);
        measureCalibrationRepo.save(measureCalibration);
        return getMeasureCalibrationResponse(id);
    }

    // 계측기 검교정 성적서 파일 삭제
    @Override
    public void deleteFileToMeasureCalibration(Long id) throws NotFoundException {
        MeasureCalibration measureCalibration = getMeasureCalibrationOrThrow(id);
        measureCalibration.setReportFileUrl(null);
        measureCalibrationRepo.save(measureCalibration);
    }
}
