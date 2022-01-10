package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.EquipmentCheckDetailRequest;
import com.mes.mesBackend.dto.response.EquipmentCheckDetailResponse;
import com.mes.mesBackend.dto.response.EquipmentCheckResponse;
import com.mes.mesBackend.service.EquipmentCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentCheckServiceImpl implements EquipmentCheckService {
    @Override
    public List<EquipmentCheckResponse> getEquipmentChecks(String equipmentType, LocalDate fromDate, LocalDate toDate) {
        return null;
    }

    @Override
    public EquipmentCheckResponse getEquipmentCheckResponse(Long equipmentId) {
        return null;
    }

    @Override
    public EquipmentCheckDetailResponse createEquipmentCheckDetail(EquipmentCheckDetailRequest equipmentCheckDetailRequest) {
        return null;
    }

    @Override
    public List<EquipmentCheckDetailResponse> getEquipmentCheckDetails(Long equipmentId) {
        return null;
    }

    @Override
    public EquipmentCheckDetailResponse getEquipmentCheckDetailResponseOrThrow(Long equipmentId, Long equipmentCheckDetailId) {
        return null;
    }

    @Override
    public EquipmentCheckDetailResponse updateEquipmentCheckDetail(Long equipmentId, Long equipmentCheckDetailId, EquipmentCheckDetailRequest equipmentCheckDetailRequest) {
        return null;
    }

    @Override
    public void deleteEquipmentCheckDetail(Long equipmentId, Long equipmentCheckDetailId) {

    }
}
