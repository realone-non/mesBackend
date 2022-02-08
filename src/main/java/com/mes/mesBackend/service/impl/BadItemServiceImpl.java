package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.BadItemRequest;
import com.mes.mesBackend.dto.response.BadItemResponse;
import com.mes.mesBackend.entity.BadItem;
import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.BadItemRepository;
import com.mes.mesBackend.repository.WorkOrderBadItemRepository;
import com.mes.mesBackend.repository.WorkProcessRepository;
import com.mes.mesBackend.service.BadItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadItemServiceImpl implements BadItemService {
    private final BadItemRepository badItemRepository;
    private final ModelMapper mapper;
    private final WorkProcessRepository workProcessRepository;
    private final WorkOrderBadItemRepository workOrderBadItemRepo;

    // 불량항목 생성
    @Override
    public BadItemResponse createBadItem(BadItemRequest badItemRequest) throws BadRequestException, NotFoundException {
        checkExistBadItemCode(badItemRequest.getBadItemCode());
        WorkProcess workProcess = getWorkProcessOrThrow(badItemRequest.getWorkProcessId());
        BadItem badItem = mapper.toEntity(badItemRequest, BadItem.class);
        badItem.add(workProcess);
        badItemRepository.save(badItem);
        return mapper.toResponse(badItem, BadItemResponse.class);
    }

    // 동일한 badItemCode가 존재하면 예외
    private void checkExistBadItemCode(String badItemCode) throws BadRequestException {
        boolean existByBadItemCode = badItemRepository.existsByBadItemCodeAndDeleteYnFalse(badItemCode);
        if (existByBadItemCode) throw new BadRequestException("same badItemCode exists. input badItemCode: " + badItemCode);
    }

    // 불량항목 단일 조회
    @Override
    public BadItemResponse getBadItem(Long id) throws NotFoundException {
        BadItem badItem = getBadItemOrThrow(id);
        return mapper.toResponse(badItem, BadItemResponse.class);
    }

    // 불량항목 전체 조회
    @Override
    public List<BadItemResponse> getBadItems(Long workProcessId) {
        List<BadItem> badItems = workOrderBadItemRepo.findBadItemByCondition(workProcessId);
        return mapper.toListResponses(badItems, BadItemResponse.class);
    }

    // 불량항목 수정
    @Override
    public BadItemResponse updateBadItem(Long id, BadItemRequest badItemRequest) throws NotFoundException, BadRequestException {
        BadItem newBadItem = mapper.toEntity(badItemRequest, BadItem.class);
        BadItem findBadItem = getBadItemOrThrow(id);
        WorkProcess newWorkProcess = getWorkProcessOrThrow(badItemRequest.getWorkProcessId());
        if (!findBadItem.getBadItemCode().equals(badItemRequest.getBadItemCode())) {
            checkExistBadItemCode(badItemRequest.getBadItemCode());
        }
        findBadItem.update(newBadItem, newWorkProcess);
        badItemRepository.save(findBadItem);
        return mapper.toResponse(findBadItem, BadItemResponse.class);
    }

    // 불량항목 삭제
    @Override
    public void deleteBadItem(Long id) throws NotFoundException {
        BadItem badItem = getBadItemOrThrow(id);
        badItem.delete();
        badItemRepository.save(badItem);
    }

    // 불량항목 단일 조회 및 예외
    @Override
    public BadItem getBadItemOrThrow(Long id) throws NotFoundException {
        return badItemRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("badItem does not exist. input id: " + id));
    }

    // 작업공정 단일 조회 및 예외
    private WorkProcess getWorkProcessOrThrow(Long workProcessId) throws NotFoundException {
        return workProcessRepository.findByIdAndDeleteYnFalse(workProcessId)
                .orElseThrow(() -> new NotFoundException("workProcess does not exist. input id: " + workProcessId));
    }
}
