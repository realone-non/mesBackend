package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.EquipmentCheckDetailRequest;
import com.mes.mesBackend.dto.response.EquipmentCheckDetailResponse;
import com.mes.mesBackend.dto.response.EquipmentCheckResponse;
import com.mes.mesBackend.entity.Equipment;
import com.mes.mesBackend.entity.EquipmentCheckDetail;
import com.mes.mesBackend.entity.EquipmentMaintenance;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.EquipmentCheckDetailRepository;
import com.mes.mesBackend.service.EquipmentCheckService;
import com.mes.mesBackend.service.EquipmentMaintenanceService;
import com.mes.mesBackend.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 17-1. 설비점검 실적 등록
@Service
@RequiredArgsConstructor
public class EquipmentCheckServiceImpl implements EquipmentCheckService {
    private final EquipmentCheckDetailRepository equipmentCheckDetailRepo;
    private final EquipmentService equipmentService;
    private final EquipmentMaintenanceService equipmentMaintenanceService;
    private final ModelMapper mapper;

    // 설비 리스트 조회
    // 검색조건: 설비유형, 점검유형(보류), 작업기간(디테일 정보 생성날짜 기준) fromDate~toDate
    @Override
    public List<EquipmentCheckResponse> getEquipmentChecks(String equipmentType, LocalDate fromDate, LocalDate toDate) {
        // TODO(점검유형 보류)
        return equipmentCheckDetailRepo.findEquipmentChecksResponseByCondition(equipmentType, fromDate, toDate);
    }

    // 설비 단일 조회
    @Override
    public EquipmentCheckResponse getEquipmentCheckResponse(Long equipmentId) throws NotFoundException {
        return equipmentCheckDetailRepo.findEquipmentChecksResponseByEquipmentId(equipmentId)
                .orElseThrow(() -> new NotFoundException("equipment does not exists. input id: " + equipmentId));
    }

    // ================================================ 설비점검 실적 상세 정보 ================================================
    // 상세정보 생성
    @Override
    public EquipmentCheckDetailResponse createEquipmentCheckDetail(
            Long equipmentId,
            EquipmentCheckDetailRequest equipmentCheckDetailRequest
    ) throws NotFoundException {
        Equipment equipment = equipmentService.getEquipmentOrThrow(equipmentId);
        EquipmentMaintenance equipmentMaintenance = equipmentMaintenanceService.getEquipmentMaintenanceOrThrow(equipmentCheckDetailRequest.getEquipmentMaintenanceId());

        EquipmentCheckDetail equipmentCheckDetail = mapper.toEntity(equipmentCheckDetailRequest, EquipmentCheckDetail.class);
        equipmentCheckDetail.add(equipment, equipmentMaintenance);

        equipmentCheckDetailRepo.save(equipmentCheckDetail);
        return getEquipmentCheckDetailResponseOrThrow(equipmentId, equipmentCheckDetail.getId());
    }

    // 상세정보 전체 조회
    @Override
    public List<EquipmentCheckDetailResponse> getEquipmentCheckDetails(Long equipmentId) {
        return equipmentCheckDetailRepo.findEquipmentCheckDetailResponseByEquipmentId(equipmentId);
    }

    // 상세정보 단일 조회
    @Override
    public EquipmentCheckDetailResponse getEquipmentCheckDetailResponseOrThrow(Long equipmentId, Long equipmentCheckDetailId) throws NotFoundException {
        return equipmentCheckDetailRepo.findEquipmentCheckDetailResponseByEquipmentIdAndEquipmentDetailId(equipmentId, equipmentCheckDetailId)
                .orElseThrow(() -> new NotFoundException("equipmentCheckDetail does not exists. input id: " + equipmentCheckDetailId));
    }

    // 상세정보 수정
    @Override
    public EquipmentCheckDetailResponse updateEquipmentCheckDetail(
            Long equipmentId,
            Long equipmentCheckDetailId,
            EquipmentCheckDetailRequest equipmentCheckDetailRequest
    ) throws NotFoundException {
        EquipmentCheckDetail findEquipmentCheckDetail = getEquipmentCheckDetailOrThrow(equipmentId, equipmentCheckDetailId);
        EquipmentMaintenance newEquipmentMaintenance = equipmentMaintenanceService.getEquipmentMaintenanceOrThrow(equipmentCheckDetailRequest.getEquipmentMaintenanceId());
        EquipmentCheckDetail newEquipmentCheckDetail = mapper.toEntity(equipmentCheckDetailRequest, EquipmentCheckDetail.class);
        findEquipmentCheckDetail.update(newEquipmentCheckDetail, newEquipmentMaintenance);
        equipmentCheckDetailRepo.save(findEquipmentCheckDetail);
        return getEquipmentCheckDetailResponseOrThrow(equipmentId, findEquipmentCheckDetail.getId());
    }

    // 상세정보 삭제
    @Override
    public void deleteEquipmentCheckDetail(Long equipmentId, Long equipmentCheckDetailId) throws NotFoundException {
        EquipmentCheckDetail equipmentCheckDetail = getEquipmentCheckDetailOrThrow(equipmentId, equipmentCheckDetailId);
        equipmentCheckDetail.delete();
        equipmentCheckDetailRepo.save(equipmentCheckDetail);
    }

    // 상세정보 단일 조회 및 예외
    private EquipmentCheckDetail getEquipmentCheckDetailOrThrow(Long equipmentId, Long equipmentCheckDetailId) throws NotFoundException {
        Equipment equipment = equipmentService.getEquipmentOrThrow(equipmentId);
        return equipmentCheckDetailRepo.findByIdAndEquipmentAndDeleteYnFalse(equipmentCheckDetailId, equipment)
                .orElseThrow(() -> new NotFoundException("equipmentCheckDetail does not exist. input id: " + equipmentCheckDetailId));
    }
}
