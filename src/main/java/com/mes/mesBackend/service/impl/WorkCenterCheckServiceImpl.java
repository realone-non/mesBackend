package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.WorkCenterCheckDetailRequest;
import com.mes.mesBackend.dto.response.WorkCenterCheckDetailResponse;
import com.mes.mesBackend.dto.response.WorkCenterCheckResponse;
import com.mes.mesBackend.entity.CheckType;
import com.mes.mesBackend.entity.WorkCenter;
import com.mes.mesBackend.entity.WorkCenterCheck;
import com.mes.mesBackend.entity.WorkCenterCheckDetail;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WorkCenterCheckDetailRepository;
import com.mes.mesBackend.repository.WorkCenterCheckRepository;
import com.mes.mesBackend.service.CheckTypeService;
import com.mes.mesBackend.service.WorkCenterCheckService;
import com.mes.mesBackend.service.WorkCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkCenterCheckServiceImpl implements WorkCenterCheckService {
    @Autowired
    WorkCenterCheckRepository workCenterCheckRepository;
    @Autowired
    WorkCenterCheckDetailRepository workCenterCheckDetailRepository;
    @Autowired
    WorkCenterService workCenterService;
    @Autowired
    CheckTypeService checkTypeService;
    @Autowired
    ModelMapper mapper;

    // 작업장별 점검유형 생성
    @Override
    public WorkCenterCheckResponse createWorkCenterCheck(Long workCenterId, Long checkTypeId) throws NotFoundException {
        WorkCenter workCenter = workCenterService.getWorkCenterOrThrow(workCenterId);
        CheckType checkType = checkTypeService.getCheckTypeOrThrow(checkTypeId);
        WorkCenterCheck workCenterCheck = new WorkCenterCheck();
        workCenterCheck.add(workCenter, checkType);
        workCenterCheckRepository.save(workCenterCheck);
        return mapper.toResponse(workCenterCheck, WorkCenterCheckResponse.class);
    }

    // 작업장별 점검유형 단일 조회
    @Override
    public WorkCenterCheckResponse getWorkCenterCheck(Long workCenterCheckId) throws NotFoundException {
        WorkCenterCheck workCenterCheck = getWorkCenterCheckOrThrow(workCenterCheckId);
        return mapper.toResponse(workCenterCheck, WorkCenterCheckResponse.class);
    }

    // 작업장별 점검유형 조회 및 예외
    private WorkCenterCheck getWorkCenterCheckOrThrow(Long id) throws NotFoundException {
        WorkCenterCheck workCenterCheck = workCenterCheckRepository.findByIdAndDeleteYnFalse(id);
        if (workCenterCheck == null) throw new NotFoundException("workCenterCheck does not exist. input id: " + id);
        return workCenterCheck;
    }

    // 작업장별 점검유형 페이징 조회/ 검색: 작업장, 점검유형
    @Override
    public Page<WorkCenterCheckResponse> getWorkCenterChecks(
            Long workCenterId,
            Long checkTypeId,
            Pageable pageable
    ) {
        Page<WorkCenterCheck> workCenterChecks = workCenterCheckRepository.findByWorkCenterAndCheckTypes(workCenterId, checkTypeId, pageable);
        return mapper.toPageResponses(workCenterChecks, WorkCenterCheckResponse.class);
    }

    // 작업장별 점검유형 수정
    @Override
    public WorkCenterCheckResponse updateWorkCenterCheck(
            Long workCenterCheckId,
            Long workCenterId,
            Long checkTypeId
    ) throws NotFoundException {
        WorkCenter newWorkCenter = workCenterService.getWorkCenterOrThrow(workCenterId);
        CheckType newCheckType = checkTypeService.getCheckTypeOrThrow(checkTypeId);
        WorkCenterCheck findWorkCenterCheck = getWorkCenterCheckOrThrow(workCenterCheckId);
        findWorkCenterCheck.add(newWorkCenter, newCheckType);
        workCenterCheckRepository.save(findWorkCenterCheck);
        return mapper.toResponse(findWorkCenterCheck, WorkCenterCheckResponse.class);
    }

    // 작업장별 점검유형 삭제
    @Override
    public void deleteWorkCenterCheck(Long id) throws NotFoundException {
        WorkCenterCheck workCenterCheck = getWorkCenterCheckOrThrow(id);
        List<WorkCenterCheckDetail> workCenterCheckDetails = workCenterCheckDetailRepository.findAllByWorkCenterCheckAndDeleteYnFalse(workCenterCheck);
        // 해당하는 세부항목 deleteYn 전부 true
        for (WorkCenterCheckDetail workCenterCheckDetail : workCenterCheckDetails) {
            workCenterCheckDetail.delete();
        }
        workCenterCheckDetailRepository.saveAll(workCenterCheckDetails);
        workCenterCheck.delete();
        workCenterCheckRepository.save(workCenterCheck);
    }

    // 작업장별 점검유형 세부 생성
    @Override
    public WorkCenterCheckDetailResponse createWorkCenterCheckDetail(
            Long workCenterCheckId,
            WorkCenterCheckDetailRequest workCenterCheckDetailRequest
    ) throws NotFoundException, BadRequestException {
        // lsl, usl 소수점 검증
        checkDecimalPoint(workCenterCheckDetailRequest.getLsl(), "lsl decimal point is 3 decimal places.");
        checkDecimalPoint(workCenterCheckDetailRequest.getUsl(), "usl decimal point is 3 decimal places.");
        WorkCenterCheck workCenterCheck = getWorkCenterCheckOrThrow(workCenterCheckId);
        WorkCenterCheckDetail workCenterCheckDetail = mapper.toEntity(workCenterCheckDetailRequest, WorkCenterCheckDetail.class);
        workCenterCheckDetail.add(workCenterCheck);
        workCenterCheckDetailRepository.save(workCenterCheckDetail);
        return mapper.toResponse(workCenterCheckDetail, WorkCenterCheckDetailResponse.class);
    }

    // 작업장별 점검유형 세부 단일 조회
    @Override
    public WorkCenterCheckDetailResponse getWorkCenterCheckDetail(
            Long workCenterCheckId,
            Long workCenterCheckDetailId
    ) throws NotFoundException {
        checkWorkCenterCheckDetail(workCenterCheckId, workCenterCheckDetailId);
        WorkCenterCheck workCenterCheck = getWorkCenterCheckOrThrow(workCenterCheckId);
        WorkCenterCheckDetail findWorkCenterCheckDetail
                = workCenterCheckDetailRepository.findByIdAndWorkCenterCheckAndDeleteYnFalse(workCenterCheckDetailId, workCenterCheck);
        return mapper.toResponse(findWorkCenterCheckDetail, WorkCenterCheckDetailResponse.class);
    }

    // workCenterCheckId와 workCenterCheckDetailId로 workCenterCheckDetail을 조회할때 존재하지 않으면 예외
    private void checkWorkCenterCheckDetail(
            Long workCenterCheckId,
            Long workCenterCheckDetailId
    ) throws NotFoundException {
        WorkCenterCheck workCenterCheck = getWorkCenterCheckOrThrow(workCenterCheckId);
        WorkCenterCheckDetail workCenterCheckDetail
                = workCenterCheckDetailRepository.findByIdAndWorkCenterCheckAndDeleteYnFalse(workCenterCheckDetailId, workCenterCheck);
        if (workCenterCheckDetail == null)
            throw new NotFoundException("workCenterDetail does not exist. input workCenterCheckId: " +workCenterCheckId + ", input workCenterCheckDetailId: " + workCenterCheckDetailId);
    }

    // 작업장별 점검유형 세부 조회 및 예외
    private WorkCenterCheckDetail getWorkCenterDetailOrThrow(Long id) throws NotFoundException {
        WorkCenterCheckDetail workCenterCheckDetail = workCenterCheckDetailRepository.findByIdAndDeleteYnFalse(id);
        if (workCenterCheckDetail == null) throw new NotFoundException("workCenterDetail does not exist. input id: " + id);
        return workCenterCheckDetail;
    }

    // 작업장별 점검유형 세부 리스트 조회(작업장 점검유형에 해당하는 모든 세부)
    @Override
    public List<WorkCenterCheckDetailResponse> getWorkCenterCheckDetails(Long workCenterCheckId) throws NotFoundException {
        WorkCenterCheck workCenterCheck = getWorkCenterCheckOrThrow(workCenterCheckId);
        List<WorkCenterCheckDetail> workCenterCheckDetails = workCenterCheckDetailRepository.findAllByWorkCenterCheckAndDeleteYnFalse(workCenterCheck);
        return mapper.toListResponses(workCenterCheckDetails, WorkCenterCheckDetailResponse.class);
    }

    // 작업장별 점검유형 세부 수정
    @Override
    public WorkCenterCheckDetailResponse updateWorkCenterCheckDetail(
            Long workCenterCheckId,
            Long workCenterCheckDetailId,
            WorkCenterCheckDetailRequest workCenterCheckDetailRequest
    ) throws NotFoundException {
        checkWorkCenterCheckDetail(workCenterCheckId, workCenterCheckDetailId);
        WorkCenterCheckDetail findWorkCenterCheckDetail = getWorkCenterDetailOrThrow(workCenterCheckDetailId);
        WorkCenterCheckDetail newWorkCenterCheckDetail = mapper.toEntity(workCenterCheckDetailRequest, WorkCenterCheckDetail.class);
        findWorkCenterCheckDetail.put(newWorkCenterCheckDetail);
        workCenterCheckDetailRepository.save(findWorkCenterCheckDetail);

        return mapper.toResponse(findWorkCenterCheckDetail, WorkCenterCheckDetailResponse.class);
    }

    // 작업장별 점검유형 세부 삭제
    @Override
    public void deleteWorkCenterCheckDetail(Long workCenterCheckId, Long workCenterCheckDetailId) throws NotFoundException {
        checkWorkCenterCheckDetail(workCenterCheckId, workCenterCheckDetailId);
        WorkCenterCheckDetail workCenterDetail = getWorkCenterDetailOrThrow(workCenterCheckDetailId);
        workCenterDetail.delete();
        workCenterCheckDetailRepository.save(workCenterDetail);
    }

    // baseScale 소수점 자리수 검증
    private void checkDecimalPoint(float decimal, String message) throws BadRequestException {
        String baseScaleToString = Float.toString(decimal);
        int indexOf = baseScaleToString.lastIndexOf(".");
        String substring = baseScaleToString.substring(indexOf);
        int length = substring.length() - 1;
        if (length > 3) throw new BadRequestException(message);
    }
}
