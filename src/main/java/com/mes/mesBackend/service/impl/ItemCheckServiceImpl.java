package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ItemCheckDetailRequest;
import com.mes.mesBackend.dto.request.ItemCheckRequest;
import com.mes.mesBackend.dto.response.ItemCheckDetailResponse;
import com.mes.mesBackend.dto.response.ItemCheckResponse;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.ItemCheck;
import com.mes.mesBackend.entity.ItemCheckDetail;
import com.mes.mesBackend.entity.enumeration.TestCategory;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ItemCheckDetailsRepository;
import com.mes.mesBackend.repository.ItemCheckRepository;
import com.mes.mesBackend.service.ItemCheckService;
import com.mes.mesBackend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCheckServiceImpl implements ItemCheckService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ItemCheckRepository itemCheckRepository;
    @Autowired
    ItemCheckDetailsRepository itemCheckDetailsRepository;
    @Autowired
    ItemService itemService;

    // 품목별 검사항목 생성
    @Override
    public ItemCheckResponse createItemCheck(ItemCheckRequest itemCheckRequest) throws NotFoundException {
        Item item = itemService.getItemOrThrow(itemCheckRequest.getItem());
        ItemCheck itemCheck = mapper.toEntity(itemCheckRequest, ItemCheck.class);
        itemCheck.addJoin(item);
        itemCheckRepository.save(itemCheck);
        return mapper.toResponse(itemCheck, ItemCheckResponse.class);
    }

    // 품목별 검사항목 단일 조회
    @Override
    public ItemCheckResponse getItemCheck(Long itemCheckId) throws NotFoundException {
        ItemCheck itemCheck = getItemCheckOrThrow(itemCheckId);
        return mapper.toResponse(itemCheck, ItemCheckResponse.class);
    }

    // 품목별 검사항목 페이징 조회 검색조건: 검사유형, 품목그룹, 품목계정
    @Override
    public Page<ItemCheckResponse> getItemChecks(TestCategory testCategory, Long itemGroupId, Long itemAccountId, Pageable pageable) {
        Page<ItemCheck> itemChecks = itemCheckRepository.findAllCondition(testCategory, itemGroupId, itemAccountId, pageable);
        return mapper.toPageResponses(itemChecks, ItemCheckResponse.class);
    }

    // 품목별 검사항목 수정
    @Override
    public ItemCheckResponse updateItemCheck(Long itemCheckId, ItemCheckRequest itemCheckRequest) throws NotFoundException {
        Item newItem = itemService.getItemOrThrow(itemCheckRequest.getItem());
        ItemCheck findItemCheck = getItemCheckOrThrow(itemCheckId);
        ItemCheck newItemCheck = mapper.toEntity(itemCheckRequest, ItemCheck.class);
        findItemCheck.update(newItemCheck, newItem);
        itemCheckRepository.save(findItemCheck);
        return mapper.toResponse(findItemCheck, ItemCheckResponse.class);
    }

    // 품목별 검사항목 삭제
    @Override
    public void deleteItemCheck(Long itemCheckId) throws NotFoundException {
        ItemCheck itemCheck = getItemCheckOrThrow(itemCheckId);
        itemCheck.delete();
        itemCheckRepository.save(itemCheck);
    }

    // 품목별 검사항목 단일 조회 및 예외
    private ItemCheck getItemCheckOrThrow(Long id) throws NotFoundException {
        return itemCheckRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("item check does not exist. input id: " + id));
    }

    // 품목별 검사항목 디테일 생성
    @Override
    public ItemCheckDetailResponse createItemCheckDetails(Long itemCheckId, ItemCheckDetailRequest itemCheckDetailRequest) throws NotFoundException {
        ItemCheck itemCheck = getItemCheckOrThrow(itemCheckId);
        ItemCheckDetail checkDetail = mapper.toEntity(itemCheckDetailRequest, ItemCheckDetail.class);
        checkDetail.addJoin(itemCheck);
        itemCheckDetailsRepository.save(checkDetail);
        return mapper.toResponse(checkDetail, ItemCheckDetailResponse.class);
    }

    // 품목별 검사항목 단일 조회
    @Override
    public ItemCheckDetailResponse getItemCheckDetail(Long itemCheckId, Long itemCheckDetailId) throws NotFoundException {
        ItemCheckDetail itemCheckDetail = getItemCheckDetailOrThrow(itemCheckId, itemCheckDetailId);
        return mapper.toResponse(itemCheckDetail, ItemCheckDetailResponse.class);
    }
    // 품목별 검사항목 디테일 리스트 조회
    @Override
    public List<ItemCheckDetailResponse> getItemCheckDetails(Long itemCheckId) throws NotFoundException {
        ItemCheck itemCheck = getItemCheckOrThrow(itemCheckId);
        List<ItemCheckDetail> itemCheckDetails = itemCheckDetailsRepository.findAllByItemCheckCategoryAndDeleteYnFalse(itemCheck);
        return mapper.toListResponses(itemCheckDetails, ItemCheckDetailResponse.class);
    }

    // 품목별 검사항목 디테일 수정
    @Override
    public ItemCheckDetailResponse updateItemCheckDetail(Long itemCheckId, Long itemCheckDetailId, ItemCheckDetailRequest itemCheckDetailRequest) throws NotFoundException {
        ItemCheckDetail findItemCheckDetail = getItemCheckDetailOrThrow(itemCheckId, itemCheckDetailId);
        ItemCheckDetail newItemCheckDetail = mapper.toEntity(itemCheckDetailRequest, ItemCheckDetail.class);
        findItemCheckDetail.update(newItemCheckDetail);
        itemCheckDetailsRepository.save(findItemCheckDetail);
        return mapper.toResponse(findItemCheckDetail, ItemCheckDetailResponse.class);
    }

    // 품목별 검사항목 디테일 삭제
    @Override
    public void deleteItemCheckDetail(Long itemCheckId, Long itemCheckDetailId) throws NotFoundException {
        ItemCheckDetail itemCheckDetail = getItemCheckDetailOrThrow(itemCheckId, itemCheckDetailId);
        itemCheckDetail.delete();
        itemCheckDetailsRepository.save(itemCheckDetail);
    }

    // 품목별 검사항목 디테일 조회 및 예외
    private ItemCheckDetail getItemCheckDetailOrThrow(Long itemCheckId, Long itemCheckDetailId) throws NotFoundException {
        ItemCheck itemCheck = getItemCheckOrThrow(itemCheckId);
        return itemCheckDetailsRepository.findByIdAndItemCheckCategoryAndDeleteYnFalse(itemCheckDetailId, itemCheck)
                .orElseThrow(() -> new NotFoundException("item check detail does not exist. input itemCheckId: "
                        + itemCheckId + ", input itemCheckDetailId: " + itemCheckDetailId));
    }
}
