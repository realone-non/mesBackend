package com.mes.mesBackend.helper;

import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.NotFoundException;

// lot log
public interface LotLogHelper {
    // lot log 생성
    void createLotLog(Long lotMasterId, Long workOrderDetailId, Long workProcessId) throws NotFoundException;
    // workProcessDivision 으로 해당 공정 id 값 찾음
    Long getWorkProcessByDivisionOrThrow(WorkProcessDivision workProcessDivision) throws NotFoundException;
    // contractItem 에 대한 작업지시 정보 찾음
    Long getWorkOrderDetailByContractItemAndWorkProcess(Long contractId, Long workProcessId) throws NotFoundException;
}
