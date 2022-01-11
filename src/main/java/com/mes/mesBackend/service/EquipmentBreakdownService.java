package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.EquipmentBreakdownRequest;
import com.mes.mesBackend.dto.request.RepairPartRequest;
import com.mes.mesBackend.dto.response.*;
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
    List<EquipmentBreakdownResponse> getEquipmentBreakdowns(Long workCenterId, Long workLineId, LocalDate fromDate, LocalDate toDate);
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
    RepairItemResponse createRepairItem(Long equipmentBreakdownId, Long repairCodeId) throws NotFoundException;
    // 수리항목 전체 조회
    List<RepairItemResponse> getRepairItemResponses(Long equipmentBreakdownId) throws NotFoundException;
    // 수리항목 단일 조회
    RepairItemResponse getRepairItemResponse(Long equipmentBreakdownId, Long repairItemId) throws NotFoundException;
    // 수리항목 수정
    RepairItemResponse updateRepairItem(Long equipmentBreakdownId, Long repairItemId, Long repairCodeId) throws NotFoundException;
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
    // ============================================== 수리작업자 정보 ==============================================
    // 수리작업자 생성
    RepairWorkerResponse createRepairWorker(Long equipmentBreakdownId, Long userId) throws NotFoundException;
    // 수리작업자 전체 조회
    List<RepairWorkerResponse> getRepairWorkerResponses(Long equipmentBreakdownId) throws NotFoundException;
    // 수리작업자 단일 조회
    RepairWorkerResponse getRepairWorkerResponse(Long equipmentBreakdownId, Long repairWorkerId) throws NotFoundException;
    // 수리작업자 수정
    RepairWorkerResponse updateRepairWorker(Long equipmentBreakdownId, Long repairWorkerId, Long userId) throws NotFoundException;
    // 수리작업자 삭제
    void deleteRepairWorker(Long equipmentBreakdownId, Long repairWorkerId) throws NotFoundException;
    // ============================================== 17-3. 설비 수리내역 조회 ==============================================
    // 설비 수리내역 리스트 조회, 검색조건: 작업장 id, 설비유형, 수리항목, 작업기간 fromDate~toDate
    List<EquipmentRepairHistoryResponse> getEquipmentRepairHistories(Long workCenterId, Long workLineId, Long repairCodeId, LocalDate fromDate, LocalDate toDate);
}
