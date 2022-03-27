package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.DeadlineResponse;
import com.mes.mesBackend.entity.Deadline;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.DeadlineRepository;
import com.mes.mesBackend.service.DeadlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 18-5. 마감일자
@Service
@RequiredArgsConstructor
public class DeadlineServiceImpl implements DeadlineService {
    private final DeadlineRepository deadlineRepository;
    private final ModelMapper mapper;

    // 마감일자 생성
    @Override
    public DeadlineResponse createDeadline(LocalDate deadlineDate) {
        Deadline deadline = new Deadline();
        deadline.setDeadline(deadlineDate);
        deadlineRepository.save(deadline);
        return mapper.toResponse(deadline, DeadlineResponse.class);
    }

    // 마감일자 단일 조회
    @Override
    public DeadlineResponse getDeadline(Long id) throws NotFoundException {
        Deadline deadline = getDeadlineOrThrow(id);
        return mapper.toResponse(deadline, DeadlineResponse.class);
    }

    // 마감일자 리스트 조회
    @Override
    public List<DeadlineResponse> getDeadlines() {
        List<Deadline> deadlines = deadlineRepository.findAllByDeleteYnFalseOrderByCreatedDateDesc();
        return mapper.toListResponses(deadlines, DeadlineResponse.class);
    }

    // 마감일자 수정
    @Override
    public DeadlineResponse updateDeadline(Long id, LocalDate deadline) throws NotFoundException {
        Deadline findDeadline = getDeadlineOrThrow(id);
        findDeadline.setDeadline(deadline);
        deadlineRepository.save(findDeadline);
        return mapper.toResponse(findDeadline, DeadlineResponse.class);
    }

    // 마감일자 삭제
    @Override
    public void deleteDeadline(Long id) throws NotFoundException {
        Deadline deadline = getDeadlineOrThrow(id);
        deadline.delete();
        deadlineRepository.save(deadline);
    }

    // 마감일자 조회 및 예외
    @Override
    public Deadline getDeadlineOrThrow(Long id) throws NotFoundException {
        return deadlineRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("deadline does not exist. input id: " + id));
    }
}
