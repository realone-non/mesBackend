package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ItemCheckDetailRequest;
import com.mes.mesBackend.dto.request.ItemCheckRequest;
import com.mes.mesBackend.dto.response.ItemCheckDetailResponse;
import com.mes.mesBackend.dto.response.ItemCheckResponse;
import com.mes.mesBackend.entity.enumeration.TestCategory;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemCheckService {
    // 품목별 검사항목 생성
    ItemCheckResponse createItemCheck(ItemCheckRequest itemCheckRequest) throws NotFoundException;
    // 품목별 검사항목 단일 조회
    ItemCheckResponse getItemCheck(Long itemCheckId) throws NotFoundException;
    // 품목별 검사항목 전체 조회
    List<ItemCheckResponse> getItemChecks(TestCategory testCategory, Long itemGroupId, Long itemAccountId);
    // 품목별 검사항목 페이징 조회
//    Page<ItemCheckResponse> getItemChecks(TestCategory testCategory, Long itemGroupId, Long itemAccountId, Pageable pageable);
    // 품목별 검사항목 수정
    ItemCheckResponse updateItemCheck(Long itemCheckId, ItemCheckRequest itemCheckRequest) throws NotFoundException;
    // 품목별 검사항목 삭제
    void deleteItemCheck(Long itemCheckId) throws NotFoundException;
    // 품목별 검사항목 디테일 생성
    ItemCheckDetailResponse createItemCheckDetails(Long itemCheckId, ItemCheckDetailRequest itemCheckDetailRequest) throws NotFoundException;
    // 품목별 검사항목 단일 조회
    ItemCheckDetailResponse getItemCheckDetail(Long itemCheckId, Long itemCheckDetailId) throws NotFoundException;
    // 품목별 검사항목 디테일 리스트 조회
    List<ItemCheckDetailResponse> getItemCheckDetails(Long itemCheckId) throws NotFoundException;
    // 품목별 검사항목 디테일 수정
    ItemCheckDetailResponse updateItemCheckDetail(Long itemCheckId, Long itemCheckDetailId, ItemCheckDetailRequest itemCheckDetailRequest) throws NotFoundException;
    // 품목별 검사항목 디테일 삭제
    void deleteItemCheckDetail(Long itemCheckId, Long itemCheckDetailId) throws NotFoundException;
}
