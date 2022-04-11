package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.response.DeadlineResponse;
import com.mes.mesBackend.entity.Deadline;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

// 18-5. 마감일자
public interface DeadlineService {
    // 마감일자 생성
    DeadlineResponse createDeadline(Long contractId, LocalDate deadlineDate) throws NotFoundException;
    // 마감일자 리스트 조회
    List<DeadlineResponse> getDeadlines();
    // 마감일자 삭제
    void deleteDeadline(Long contractId) throws NotFoundException, BadRequestException;
}
