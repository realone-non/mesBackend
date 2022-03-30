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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadItemServiceImpl implements BadItemService {
    private final BadItemRepository badItemRepository;
    private final ModelMapper mapper;
    private final WorkProcessRepository workProcessRepository;
    private final WorkOrderBadItemRepository workOrderBadItemRepo;
    private final WorkOrderBadItemRepository workOrderBadItemRepository;

    // 불량항목 생성
    @Override
    public BadItemResponse createBadItem(BadItemRequest badItemRequest) throws BadRequestException, NotFoundException {
        // 동일한 badItemCode가 존재하면 예외
        checkExistBadItemCode(badItemRequest.getBadItemCode());

        WorkProcess workProcess = getWorkProcessOrThrow(badItemRequest.getWorkProcessId());
        BadItem badItem = mapper.toEntity(badItemRequest, BadItem.class);
        badItem.add(workProcess);
        badItemRepository.save(badItem);
        return mapper.toResponse(badItem, BadItemResponse.class);
    }

    // 불량항목 단일 조회
    @Override
    public BadItemResponse getBadItem(Long id) throws NotFoundException {
        BadItem badItem = getBadItemOrThrow(id);
        return mapper.toResponse(badItem, BadItemResponse.class);
    }

    // 불량항목 전체 조회
    // 정렬조건 1. 공정별 순번 asc, 2. 순번정렬 asc
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

        // 코드가 수정되었으면 기존 코드 존재하는 코드인지 체크
        if (!findBadItem.getBadItemCode().equals(badItemRequest.getBadItemCode())) checkExistBadItemCode(badItemRequest.getBadItemCode());
        // 작업공정이 변경 되었을때 불량정보 등록되어 있으면 공정수정 불가능
        if (!findBadItem.getWorkProcess().getId().equals(newWorkProcess.getId())) throwIfExistsBadItemEnrollmentIsWorkProcessNotUpdate(newBadItem.getId());

        findBadItem.update(newBadItem, newWorkProcess);
        badItemRepository.save(findBadItem);
        return mapper.toResponse(findBadItem, BadItemResponse.class);
    }

    // 불량항목 삭제
    @Override
    public void deleteBadItem(Long id) throws NotFoundException, BadRequestException {
        BadItem badItem = getBadItemOrThrow(id);
        // 불량정보 등록되어 있으면 삭제 불가능
        throwIfExistsBadItemEnrollmentIsNotDelete(badItem.getId());

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

    // 동일한 badItemCode가 존재하면 예외
    private void checkExistBadItemCode(String badItemCode) throws BadRequestException {
        boolean existByBadItemCode = badItemRepository.existsByBadItemCodeAndDeleteYnFalse(badItemCode);
        if (existByBadItemCode) throw new BadRequestException("입력한 불량코드는 이미 등록되어있습니다. 다른 불량코드를 입력해주세요.");
    }

    // 불량정보 등록되어 있으면 삭제 불가능
    private void throwIfExistsBadItemEnrollmentIsNotDelete(Long badItemId) throws BadRequestException {
        boolean exists = workOrderBadItemRepository.existByBadItemAndDeleteYnFalse(badItemId);
        if (exists) throw new BadRequestException("해당 불량유형은 불량정보로 등록되어 있으므로 삭제가 불가능합니다.");
    }

    // 불량정보 등록되어 있으면 공정수정 불가능
    private void throwIfExistsBadItemEnrollmentIsWorkProcessNotUpdate(Long badItemId) throws BadRequestException {
        boolean exists = workOrderBadItemRepository.existByBadItemAndDeleteYnFalse(badItemId);
        if (exists) throw new BadRequestException("해당 불량유형은 불량정ㅂ로 등록되어 있으므로 공정정보 수정이 불가능합니다.");
    }
}
