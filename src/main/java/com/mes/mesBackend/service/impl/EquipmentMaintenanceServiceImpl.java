package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.EquipmentMaintenanceRequest;
import com.mes.mesBackend.dto.response.EquipmentMaintenanceResponse;
import com.mes.mesBackend.entity.EquipmentMaintenance;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.EquipmentMaintenanceRepository;
import com.mes.mesBackend.service.EquipmentMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

// 3-5-2. 설비 보전항목 등록
@Service
public class EquipmentMaintenanceServiceImpl implements EquipmentMaintenanceService {
    @Autowired
    EquipmentMaintenanceRepository equipmentMaintenanceRepository;
    @Autowired
    ModelMapper mapper;
    
    // 설비 보전항목 생성
    @Override
    public EquipmentMaintenanceResponse createEquipmentMaintenance(EquipmentMaintenanceRequest equipmentMaintenanceRequest) {
        EquipmentMaintenance equipmentMaintenance = mapper.toEntity(equipmentMaintenanceRequest, EquipmentMaintenance.class);
        equipmentMaintenanceRepository.save(equipmentMaintenance);
        return mapper.toResponse(equipmentMaintenance, EquipmentMaintenanceResponse.class);
    }
    // 설비 보전항목 단일 조회
    @Override
    public EquipmentMaintenanceResponse getEquipmentMaintenance(Long id) throws NotFoundException {
        EquipmentMaintenance equipmentMaintenance = getEquipmentMaintenanceOrThrow(id);
        return mapper.toResponse(equipmentMaintenance, EquipmentMaintenanceResponse.class);
    }
    // 설비 보전항목 전체 조회
    @Override
    public List<EquipmentMaintenanceResponse> getEquipmentMaintenances() {
        List<EquipmentMaintenance> equipmentMaintenances = equipmentMaintenanceRepository.findAllByDeleteYnFalse();
        return mapper.toListResponses(equipmentMaintenances, EquipmentMaintenanceResponse.class);
    }
//    public Page<EquipmentMaintenanceResponse> getEquipmentMaintenances(Pageable pageable) {
//        Page<EquipmentMaintenance> equipmentMaintenances = equipmentMaintenanceRepository.findAllByDeleteYnFalse(pageable);
//        return mapper.toPageResponses(equipmentMaintenances, EquipmentMaintenanceResponse.class);
//    }

    // 설비 보전항목 수정
    @Override
    public EquipmentMaintenanceResponse updateEquipmentMaintenance(Long id, EquipmentMaintenanceRequest equipmentMaintenanceRequest) throws NotFoundException {
        EquipmentMaintenance newEquipmentMaintenance = mapper.toEntity(equipmentMaintenanceRequest, EquipmentMaintenance.class);
        EquipmentMaintenance findEquipmentMaintenance = getEquipmentMaintenanceOrThrow(id);
        findEquipmentMaintenance.update(newEquipmentMaintenance);
        equipmentMaintenanceRepository.save(findEquipmentMaintenance);
        return mapper.toResponse(findEquipmentMaintenance, EquipmentMaintenanceResponse.class);
    }
    // 설비 보전항목 삭제
    @Override
    public void deleteEquipmentMaintenance(Long id) throws NotFoundException {
        EquipmentMaintenance equipmentMaintenance = getEquipmentMaintenanceOrThrow(id);
        equipmentMaintenance.delete();
        equipmentMaintenanceRepository.save(equipmentMaintenance);
    }

    // 설비 보전항목 단일 조회 및 예외
    private EquipmentMaintenance getEquipmentMaintenanceOrThrow(Long id) throws NotFoundException {
        return equipmentMaintenanceRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("equipment maintenance does not exist. input id: " + id));
    }
}
