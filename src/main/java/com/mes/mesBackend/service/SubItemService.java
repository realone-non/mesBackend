package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.SubItemRequest;
import com.mes.mesBackend.dto.response.SubItemResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// 3-2-4. 대체품 등록
public interface SubItemService {
    // 대체품 생성
    SubItemResponse createSubItem(SubItemRequest subItemRequest) throws NotFoundException, BadRequestException;
    // 대체품 단일 조회
    SubItemResponse getSubItem(Long id) throws NotFoundException;
    // 대체품 페이징 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
    Page<SubItemResponse> getSubItems(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, Pageable pageable);
    // 대체품 수정
    SubItemResponse updateSubItem(Long id, SubItemRequest subItemRequest) throws NotFoundException, BadRequestException;
    // 대체품 삭제
    void deleteSubItem(Long id) throws NotFoundException;
}
