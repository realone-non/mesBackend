package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.DeadlineResponse;
import com.mes.mesBackend.entity.Deadline;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 18-5. 마감일자
public interface DeadlineService {
    // 마감일자 생성
    DeadlineResponse createDeadline(LocalDate endDate);
    // 마감일자 단일 조회
    DeadlineResponse getDeadline(Long id) throws NotFoundException;
    // 마감일자 리스트 조회
    List<DeadlineResponse> getDeadlines();
    // 마감일자 수정
    DeadlineResponse updateDeadline(Long id, LocalDate endDate) throws NotFoundException;
    // 마감일자 삭제
    void deleteDeadline(Long id) throws NotFoundException;
    // 마감일자 조회 및 예외
    Deadline getDeadlineOrThrow(Long id) throws NotFoundException;
}
