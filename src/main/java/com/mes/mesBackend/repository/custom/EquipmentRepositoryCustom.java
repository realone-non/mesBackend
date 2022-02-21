package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.PopEquipmentResponse;
import com.mes.mesBackend.entity.Equipment;

import javax.swing.*;
import java.util.List;

public interface EquipmentRepositoryCustom {
    // 작업공정에 따라 설비 조회
    List<PopEquipmentResponse> findPopEquipmentResponseByWorkProcess(Long workProcessId);

    // 작업공정으로 단일 설비 조회
    Equipment findByWorkProcess(Long workProcessId);

    List<Equipment> findByCondition(String equipmentName);
}
