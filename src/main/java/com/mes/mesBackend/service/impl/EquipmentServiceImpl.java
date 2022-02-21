package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.EquipmentRequest;
import com.mes.mesBackend.dto.response.EquipmentResponse;
import com.mes.mesBackend.dto.response.PopEquipmentResponse;
import com.mes.mesBackend.entity.Client;
import com.mes.mesBackend.entity.Equipment;
import com.mes.mesBackend.entity.WorkLine;
import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.EquipmentRepository;
import com.mes.mesBackend.service.ClientService;
import com.mes.mesBackend.service.EquipmentService;
import com.mes.mesBackend.service.WorkLineService;
import com.mes.mesBackend.service.WorkProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 3-5-1. 설비등록
@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final WorkLineService workLineService;
    private final ModelMapper mapper;
    private final ClientService clientService;
    private final WorkProcessService workProcessService;

    // 설비 생성
    @Override
    public EquipmentResponse createEquipment(EquipmentRequest equipmentRequest) throws NotFoundException {
        Client client = clientService.getClientOrThrow(equipmentRequest.getClient());
        WorkLine workLine = workLineService.getWorkLineOrThrow(equipmentRequest.getWorkLine());
        WorkProcess workProcess = workProcessService.getWorkProcessOrThrow(equipmentRequest.getWorkProcessId());
        Equipment equipment = mapper.toEntity(equipmentRequest, Equipment.class);
        equipment.addJoin(client, workLine, workProcess);
        equipmentRepository.save(equipment);
        return mapper.toResponse(equipment, EquipmentResponse.class);
    }

    // 설비 단일 조회
    @Override
    public EquipmentResponse getEquipment(Long id) throws NotFoundException {
        Equipment equipment = getEquipmentOrThrow(id);
        return mapper.toResponse(equipment, EquipmentResponse.class);
    }

    // 설비 전체 조회
    @Override
    public List<EquipmentResponse> getEquipments(String equipmentName) {
        List<Equipment> equipments = equipmentRepository.findByCondition(equipmentName);
        return mapper.toListResponses(equipments, EquipmentResponse.class);
    }
//    public Page<EquipmentResponse> getEquipments(Pageable pageable) {
//        Page<Equipment> equipments = equipmentRepository.findAllByDeleteYnFalse(pageable);
//        return mapper.toPageResponses(equipments, EquipmentResponse.class);
//    }

    // 설비 수정
    @Override
    public EquipmentResponse updateEquipment(Long id, EquipmentRequest equipmentRequest) throws NotFoundException {
        Equipment findEquipment = getEquipmentOrThrow(id);
        Client newClient = clientService.getClientOrThrow(equipmentRequest.getClient());
        WorkLine newWorkLine = workLineService.getWorkLineOrThrow(equipmentRequest.getWorkLine());
        WorkProcess workProcess = workProcessService.getWorkProcessOrThrow(equipmentRequest.getWorkProcessId());
        Equipment newEquipment = mapper.toEntity(equipmentRequest, Equipment.class);
        findEquipment.update(newEquipment, newClient, newWorkLine, workProcess);
        equipmentRepository.save(findEquipment);
        return mapper.toResponse(findEquipment, EquipmentResponse.class);
    }

    // 설비 삭제
    @Override
    public void deleteEquipment(Long id) throws NotFoundException {
        Equipment equipment = getEquipmentOrThrow(id);
        equipment.delete();
        equipmentRepository.save(equipment);
    }

    // 설비 단일 조회 및 예외
    @Override
    public Equipment getEquipmentOrThrow(Long id) throws NotFoundException {
        return equipmentRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("equipment does not exist. input id: " + id));
    }

    //공정 id로 설비 조회
    public List<PopEquipmentResponse> getEquipmentsByWorkProcess(Long workProcessId) throws NotFoundException {
        return equipmentRepository.findPopEquipmentResponseByWorkProcess(workProcessId);
    }
}
