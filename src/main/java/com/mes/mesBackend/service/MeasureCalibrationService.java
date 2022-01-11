package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.MeasureCalibrationRequest;
import com.mes.mesBackend.dto.response.MeasureCalibrationResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

// 17-5. 계측기 검교정 실적 등록
public interface MeasureCalibrationService {
    // 계측기 검교정 실적 생성
    MeasureCalibrationResponse createMeasureCalibration(MeasureCalibrationRequest measureCalibrationRequest) throws NotFoundException;
    // 계측기 검교정 실적 단일 조회
    MeasureCalibrationResponse getMeasureCalibrationResponse(Long id) throws NotFoundException;
    // 계측기 검교정 실적 리스트 검색 조회, 검색조건: 검정처(부서 id), 측정기유형(계측기유형), 검정기간(검교정일자) fromDate~toDate
    List<MeasureCalibrationResponse> getMeasureCalibrations(Long departmentId, Long gaugeTypeId, LocalDate fromDate, LocalDate toDate);
    // 계측기 검교정 실적 수정
    MeasureCalibrationResponse updateMeasureCalibration(Long id, MeasureCalibrationRequest measureCalibrationRequest) throws NotFoundException;
    // 계측기 검교정 실적 삭제
    void deleteMeasureCalibration(Long id) throws NotFoundException;
    // 계측기 검교정 성적서 파일 생성
    MeasureCalibrationResponse createFilesToMeasureCalibration(Long id, MultipartFile file) throws NotFoundException, BadRequestException, IOException;
    // 계측기 검교정 성적서 파일 삭제
    void deleteFileToMeasureCalibration(Long id) throws NotFoundException;
}
