package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.EquipmentBreakdownRequest;
import com.mes.mesBackend.dto.request.RepairItemRequest;
import com.mes.mesBackend.dto.request.RepairPartRequest;
import com.mes.mesBackend.dto.response.EquipmentBreakdownResponse;
import com.mes.mesBackend.dto.response.RepairItemResponse;
import com.mes.mesBackend.dto.response.RepairPartResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

// 17-2. 설비 고장 수리내역 등록
public interface EquipmentBreakdownService {
    // 설비고장 생성
    EquipmentBreakdownResponse createEquipmentBreakdown(EquipmentBreakdownRequest equipmentBreakdownRequest) throws NotFoundException;
    // 설비고장 리스트 검색 조회, 검색조건: 작업장 id, 설비유형, 작업기간 fromDate~toDate
    List<EquipmentBreakdownResponse> getEquipmentBreakdowns(Long workCenterId, String equipmentType, LocalDate fromDate, LocalDate toDate);
    // 설비고장 단일조회
    EquipmentBreakdownResponse getEquipmentBreakdown(Long equipmentBreakdownId) throws NotFoundException;
    // 설비고장 수정
    EquipmentBreakdownResponse updateEquipmentBreakdown(Long equipmentBreakdownId, EquipmentBreakdownRequest equipmentBreakdownRequest) throws NotFoundException;
    // 설비고장 삭제
    void deleteEquipmentBreakdown(Long equipmentBreakdownId) throws NotFoundException;
    // 설비고장 파일 생성
    EquipmentBreakdownResponse createFilesToEquipmentBreakdown(Long equipmentBreakdownId, boolean fileDivision, List<MultipartFile> files) throws NotFoundException, BadRequestException, IOException;
    // 설비고장 파일 삭제
    void deleteFileToEquipmentBreakdown(Long equipmentBreakdownId, Long fileId) throws NotFoundException;
    // ============================================== 수리항목 ==============================================
    // 수리항목 생성
    RepairItemResponse createRepairItem(Long equipmentBreakdownId, RepairItemRequest repairItemRequest) throws NotFoundException;
    // 수리항목 전체 조회
    List<RepairItemResponse> getRepairItemResponses(Long equipmentBreakdownId) throws NotFoundException;
    // 수리항목 단일 조회
    RepairItemResponse getRepairItemResponse(Long equipmentBreakdownId, Long repairItemId) throws NotFoundException;
    // 수리항목 수정
    RepairItemResponse updateRepairItem(Long equipmentBreakdownId, Long repairItemId, RepairItemRequest repairItemRequest) throws NotFoundException;
    // 수리항목 삭제
    void deleteRepairItem(Long equipmentBreakdownId, Long repairItemId) throws NotFoundException;
    // ============================================== 수리부품정보 ==============================================
    // 수리부품 생성
    RepairPartResponse createRepairPart(Long equipmentBreakdownId, Long repairItemId, RepairPartRequest repairPartRequest) throws NotFoundException;
    // 수리부품 리스트 조회
    List<RepairPartResponse> getRepairPartResponses(Long equipmentBreakdownId, Long repairItemId) throws NotFoundException;
    // 수리부품 단일 조회
    RepairPartResponse getRepairPartResponse(Long equipmentBreakdownId, Long repairItemId, Long repairPartId) throws NotFoundException;
    // 수리부품 수정
    RepairPartResponse updateRepairPart(Long equipmentBreakdownId, Long repairItemId, Long repairPartId, RepairPartRequest repairPartRequest) throws NotFoundException;
    // 수리부품 삭제
    void deleteRepairPart(Long equipmentBreakdownId, Long repairItemId, Long repairPartId) throws NotFoundException;
}
