package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.Equipment;

public interface EquipmentRepositoryCustom {
    //공정으로 설비 가져오기
     Equipment findByWorkProcess(Long workProcessId);
}
