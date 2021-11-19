package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.BadItemRequest;
import com.mes.mesBackend.dto.response.BadItemResponse;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BadItemService {
    // 불량항목 생성
    BadItemResponse createBadItem(BadItemRequest badItemRequest);
    // 불량항목 단일 조회
    BadItemResponse getBadItem(Long id) throws NotFoundException;
    // 불량항목 페이징 조회
    Page<BadItemResponse> getBadItems(Pageable pageable);
    // 불량항목 수정
    BadItemResponse updateBadItem(Long id, BadItemRequest badItemRequest) throws NotFoundException;
    // 불량항목 삭제
    void deleteBadItem(Long id) throws NotFoundException;
}
