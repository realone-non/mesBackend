package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.PopEquipmentResponse;

import java.util.List;

public interface EquipmentRepositoryCustom {
    // 작업공정에 따라 설비 조회
    List<PopEquipmentResponse> findPopEquipmentResponseByWorkProcess(Long workProcessId);
}
