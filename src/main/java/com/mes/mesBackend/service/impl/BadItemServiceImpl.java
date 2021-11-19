package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.BadItemRequest;
import com.mes.mesBackend.dto.response.BadItemResponse;
import com.mes.mesBackend.entity.BadItem;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.BadItemRepository;
import com.mes.mesBackend.service.BadItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BadItemServiceImpl implements BadItemService {
    @Autowired
    BadItemRepository badItemRepository;
    @Autowired
    ModelMapper mapper;

    // 불량항목 생성
    @Override
    public BadItemResponse createBadItem(BadItemRequest badItemRequest) {
        BadItem badItem = mapper.toEntity(badItemRequest, BadItem.class);
        badItemRepository.save(badItem);
        return mapper.toResponse(badItem, BadItemResponse.class);

    }
    // 불량항목 단일 조회
    @Override
    public BadItemResponse getBadItem(Long id) throws NotFoundException {
        BadItem badItem = getBadItemOrThrow(id);
        return mapper.toResponse(badItem, BadItemResponse.class);
    }
    // 불량항목 페이징 조회
    @Override
    public Page<BadItemResponse> getBadItems(Pageable pageable) {
        Page<BadItem> badItems = badItemRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(badItems, BadItemResponse.class);
    }
    // 불량항목 수정
    @Override
    public BadItemResponse updateBadItem(Long id, BadItemRequest badItemRequest) throws NotFoundException {
        BadItem newBadItem = mapper.toEntity(badItemRequest, BadItem.class);
        BadItem findBadItem = getBadItemOrThrow(id);
        findBadItem.update(newBadItem);
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
    private BadItem getBadItemOrThrow(Long id) throws NotFoundException {
        return badItemRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("badItem does not exist. input id: " + id));
    }
}
