package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.RepairCodeRequest;
import com.mes.mesBackend.dto.response.RepairCodeResponse;
import com.mes.mesBackend.entity.RepairCode;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

public interface RepairCodeService {
    // 수리코드 생성
    RepairCodeResponse createRepairCode(RepairCodeRequest repairCodeRequest);
    // 수리코드 단일 조회
    RepairCodeResponse getRepairCode(Long id) throws NotFoundException;
    // 수리코드 전체 조회
    List<RepairCodeResponse> getRepairCodes();
    // 수리코드 수정
    RepairCodeResponse updateRepairCode(Long id, RepairCodeRequest repairCodeRequest) throws NotFoundException;
    // 수리코드 삭제
    void deleteRepairCode(Long id) throws NotFoundException;
    // 수리코드 단일 조회 및 예외
    RepairCode getRepairCodeOrThrow(Long id) throws NotFoundException;
}
