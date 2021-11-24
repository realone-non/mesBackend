package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.EquipmentRequest;
import com.mes.mesBackend.dto.response.EquipmentResponse;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 3-5-1. 설비등록
public interface EquipmentService {
    // 설비 생성
    EquipmentResponse createEquipment(EquipmentRequest equipmentRequest) throws NotFoundException;
    // 설비 단일 조회
    EquipmentResponse getEquipment(Long id) throws NotFoundException;
    // 설비 전체 조회
    List<EquipmentResponse> getEquipments();
//    Page<EquipmentResponse> getEquipments(Pageable pageable);
    // 설비 수정
    EquipmentResponse updateEquipment(Long id, EquipmentRequest equipmentRequest) throws NotFoundException;
    // 설비 삭제
    void deleteEquipment(Long id) throws NotFoundException;
}
