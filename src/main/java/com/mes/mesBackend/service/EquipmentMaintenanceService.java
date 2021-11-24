package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.EquipmentMaintenanceRequest;
import com.mes.mesBackend.dto.response.EquipmentMaintenanceResponse;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 3-5-2. 설비 보전항목 등록
public interface EquipmentMaintenanceService {
    // 설비 보전항목 생성
    EquipmentMaintenanceResponse createEquipmentMaintenance(EquipmentMaintenanceRequest equipmentMaintenanceRequest);
    // 설비 보전항목 단일 조회
    EquipmentMaintenanceResponse getEquipmentMaintenance(Long id) throws NotFoundException;
    // 설비 보전항목 전체 조회
    List<EquipmentMaintenanceResponse> getEquipmentMaintenances();
//    Page<EquipmentMaintenanceResponse> getEquipmentMaintenances(Pageable pageable);
    // 설비 보전항목 수정
    EquipmentMaintenanceResponse updateEquipmentMaintenance(Long id, EquipmentMaintenanceRequest equipmentMaintenanceRequest) throws NotFoundException;
    // 설비 보전항목 삭제
    void deleteEquipmentMaintenance(Long id) throws NotFoundException;
}
