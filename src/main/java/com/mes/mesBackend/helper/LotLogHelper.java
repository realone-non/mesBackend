package com.mes.mesBackend.helper;

import com.mes.mesBackend.entity.LotLog;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.NotFoundException;

// lot log
// 작업지시가 SCHEDULE 에서 ONGOING 으로 변경되는 시점에서 사용
public interface LotLogHelper {
    // lot log 생성
    void createLotLog(Long lotMasterId, Long workOrderDetailId, Long workProcessId) throws NotFoundException;
    // workProcessDivision 으로 해당 공정 id 값 찾음
    Long getWorkProcessByDivisionOrThrow(WorkProcessDivision workProcessDivision) throws NotFoundException;
    // contractItem 에 대한 작업지시 정보 찾음
    Long getWorkOrderDetailByContractItemAndWorkProcess(Long contractId, Long workProcessId) throws NotFoundException;
    // workOrderDetail id, workProcess id 로 LotLog 찾음
    LotLog getLotLogByWorkOrderDetailIdAndWorkProcessIdOrThrow(Long workOrderDetailId, Long workProcessId) throws NotFoundException;

}
