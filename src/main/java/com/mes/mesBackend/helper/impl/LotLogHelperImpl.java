package com.mes.mesBackend.helper.impl;

import com.mes.mesBackend.entity.LotLog;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.WorkOrderDetail;
import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.LotLogHelper;
import com.mes.mesBackend.repository.LotLogRepository;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.WorkOrderDetailRepository;
import com.mes.mesBackend.repository.WorkProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.mes.mesBackend.entity.enumeration.WorkProcessDivision.SHIPMENT;

// lotLog
@Component
@RequiredArgsConstructor
public class LotLogHelperImpl implements LotLogHelper {
    private final LotLogRepository lotLogRepo;
    private final LotMasterRepository lotMasterRepo;
    private final WorkOrderDetailRepository workOrderDetailRepo;
    private final WorkProcessRepository workProcessRepo;

    // lotLog 생성
    @Override
    public void createLotLog(Long lotMasterId, Long workOrderDetailId, Long workProcessId) throws NotFoundException {
        LotMaster lotMaster = getLotMasterOrThrow(lotMasterId);
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderDetailId);
        WorkProcess workProcess = getWorkProcessOrThrow(workProcessId);
        LotLog lotLog = new LotLog();
        lotLog.setLotMaster(lotMaster);
        lotLog.setWorkOrderDetail(workOrderDetail);
        lotLog.setWorkProcess(workProcess);
        lotLogRepo.save(lotLog);
    }

    // workProcessDivision 으로 해당 공정 id 값 찾음
    @Override
    public Long getWorkProcessByDivisionOrThrow(WorkProcessDivision workProcessDivision) throws NotFoundException {
        return lotLogRepo.findWorkProcessIdByWorkProcessDivision(workProcessDivision)
                .orElseThrow(() -> new NotFoundException("[LotLogHelper] 해당하는 workProcessDivision 에 대한 작업공정이 존재하지 않음."));
    }

    // 수주품목, 작업공정으로 해당 작업지시 찾음
    @Override
    public Long getWorkOrderDetailByContractItemAndWorkProcess(Long contractItemId, Long workProcessId) throws NotFoundException {
        return lotLogRepo.findWorkOrderDetailIdByContractItemAndWorkProcess(contractItemId, workProcessId)
                .orElseThrow(() -> new NotFoundException("[LotLogHelper] 해당하는 수주품목과 작업공정에 대한 작업지시 정보가 존재하지 않음. " +
                        "contractId: " + contractItemId + "," +
                        " workProcessId: " + workProcessId)
                );
    }

    // workOrderDetail id, workProcess id 로 LotLog 찾음
    @Override
    public LotLog getLotLogByWorkOrderDetailIdAndWorkProcessIdOrThrow(Long workOrderDetailId, Long workProcessId) throws NotFoundException {
        WorkOrderDetail workOrderDetail = getWorkOrderDetailOrThrow(workOrderDetailId);
        WorkProcess workProcess = getWorkProcessOrThrow(workProcessId);
        return lotLogRepo.findByWorkOrderDetailAndWorkProcess(workOrderDetail, workProcess)
                .orElseThrow(() -> new NotFoundException("[LotLogHelper] 해당 작업공정과 작업지시에 해당하는 lotMaster 가 없습니다. "));
    }

    @Override
    public LotLog getLotLogByWorkOrderDetailOrThrow(Long workOrderDetailId) throws NotFoundException {
        return lotLogRepo.findByWorkOrderDetailId(workOrderDetailId)
                .orElseThrow(() -> new NotFoundException("[데이터 오류] 해당 작업지시에 맞는 dummy lot 가 존재하지 않습니다."));
    }

    private LotMaster getLotMasterOrThrow(Long id) throws NotFoundException {
        return lotMasterRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[LotLogHelper] lotMaster does not exist. input id:" + id));
    }

    private WorkOrderDetail getWorkOrderDetailOrThrow(Long id) throws NotFoundException {
        return workOrderDetailRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[LotLogHelper] workOrderDetail does not exist. input id: " + id));
    }

    private WorkProcess getWorkProcessOrThrow(Long id) throws NotFoundException {
        return workProcessRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("[LotLogHelper] workProcess does not exist. input id: " + id));
    }
}
