package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.BomItemRequest;
import com.mes.mesBackend.dto.request.BomMasterRequest;
import com.mes.mesBackend.dto.response.BomItemDetailResponse;
import com.mes.mesBackend.dto.response.BomItemResponse;
import com.mes.mesBackend.dto.response.BomMasterResponse;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BomMasterService {
    // BOM 마스터 생성
    BomMasterResponse createBomMaster(BomMasterRequest bomMasterRequest) throws NotFoundException;
    // BOM 마스처 단일 조회
    BomMasterResponse getBomMaster(Long bomMasterId) throws NotFoundException;
    // BOM 마스터 페이징 조회 검색조건: 품목계정, 품목그룹, 품번|품명
//    Page<BomMasterResponse> getBomMasters(Long itemAccountId, Long itemGroupId, String itemNoAndItemName, Pageable pageable);
    // Bom 마스터 전체 조회 검색조건: 품목계정, 품목그룹, 품번|품명
    List<BomMasterResponse> getBomMasters(Long itemAccountId, Long itemGroupId, String itemNoAndItemName);
    // BOM 마스터 수정
    BomMasterResponse updateBomMaster(Long bomMasterId, BomMasterRequest bomMasterRequest) throws NotFoundException;
    // BOM 마스터 삭제
    void deleteBomMaster(Long bomMasterId) throws NotFoundException;


    // BOM 품목 생성
    BomItemResponse createBomItem(Long bomMasterId, BomItemRequest bomMasterDetailRequest) throws NotFoundException;
    // BOM 품목 리스트 조회
    List<BomItemDetailResponse> getBomItems(Long bomMasterId, String itemNoOrItemName) throws NotFoundException;
    // BOM 품목 수정
    BomItemResponse updateBomItem(Long bomMasterId, Long bomItemId, BomItemRequest bomMasterDetailRequest) throws NotFoundException;
    // BOM 품목 삭제
    void deleteBomItem(Long bomMasterId, Long bomItemId) throws NotFoundException;
    // BOM 품목 단일 조회
    BomItemResponse getBomItem(Long bomMasterId, Long bomItemId) throws NotFoundException;
}
