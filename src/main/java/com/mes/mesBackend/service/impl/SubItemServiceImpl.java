package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.SubItemRequest;
import com.mes.mesBackend.dto.response.SubItemResponse;
import com.mes.mesBackend.entity.Item;
import com.mes.mesBackend.entity.SubItem;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.SubItemRepository;
import com.mes.mesBackend.service.ItemService;
import com.mes.mesBackend.service.SubItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubItemServiceImpl implements SubItemService {

    @Autowired
    SubItemRepository subItemRepository;
    @Autowired
    ItemService itemService;

    @Autowired
    ModelMapper mapper;

    // 대체품 생성
    @Override
    public SubItemResponse createSubItem(SubItemRequest subItemRequest) throws NotFoundException, BadRequestException {
        Item inputItem = itemService.getItemOrThrow(subItemRequest.getItem());
        Item inputSubItem = itemService.getItemOrThrow(subItemRequest.getSubItem());
        throwIfItemAndSubItemSame(inputItem, inputSubItem);
//        checkSubItemOrder(inputItem, inputSubItem, subItemRequest.getSubOrders());
        SubItem subItem = mapper.toEntity(subItemRequest, SubItem.class);
        subItem.addJoin(inputItem, inputSubItem);
        subItemRepository.save(subItem);
        return mapper.toResponse(subItem, SubItemResponse.class);
    }

    // item과 subItem이 같을경우 예외
    private void throwIfItemAndSubItemSame(Item item, Item subItem) throws BadRequestException {
        if (item.equals(subItem)) {
            throw new BadRequestException("품목과 대체품목이 같을 수 없습니다. 확인 후 다시 시도해주세요.");
        }
    }

    // 같은 품목, 대체품목이 존재할때 순서가 같으면 예외
    private void checkSubItemOrder(Item item, Item subItem, int subItemOrder) throws BadRequestException {
        boolean exists = subItemRepository.existsByItemAndSubItemAndDeleteYnFalseAndSubOrders(item, subItem, subItemOrder);
        if (exists) {
            throw new BadRequestException("품목과 대체품목이 같을 수 없습니다. 확인 후 다시 시도해주세요.");
        }
    }

    // 대체품 단일 조회
    @Override
    public SubItemResponse getSubItem(Long id) throws NotFoundException {
        SubItem subItem = getSubItemOrThrow(id);
        return mapper.toResponse(subItem, SubItemResponse.class);
    }

    // 대체품 단일 조회 및 예외
    private SubItem getSubItemOrThrow(Long id) throws NotFoundException {
        return subItemRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("subItem does not exist. input id: " + id));
    }

    // 대체품 전체 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
    @Override
    public List<SubItemResponse> getSubItems(Long itemGroupId, Long itemAccountId, String itemNo, String itemName) {
        List<SubItem> subItems = subItemRepository.findAllCondition(itemGroupId, itemAccountId, itemNo, itemName);
        return mapper.toListResponses(subItems, SubItemResponse.class);
    }

    // 대체품 페이징 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
//    @Override
//    public Page<SubItemResponse> getSubItems(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, Pageable pageable) {
//        Page<SubItem> subItems = subItemRepository.findAllCondition(itemGroupId, itemAccountId, itemNo, itemName, pageable);
//        return mapper.toPageResponses(subItems, SubItemResponse.class);
//    }

    // 대체품 수정
    @Override
    public SubItemResponse updateSubItem(Long id, SubItemRequest subItemRequest) throws NotFoundException, BadRequestException {
        Item newInputItem = itemService.getItemOrThrow(subItemRequest.getItem());
        Item newInputSubItem = itemService.getItemOrThrow(subItemRequest.getSubItem());
        throwIfItemAndSubItemSame(newInputItem, newInputSubItem);
//        checkSubItemOrder(newInputItem, newInputSubItem, subItemRequest.getSubOrders());
        SubItem findSubItem = getSubItemOrThrow(id);
        SubItem newSubItemEntity = mapper.toEntity(subItemRequest, SubItem.class);

        findSubItem.update(newSubItemEntity, newInputItem, newInputSubItem);
        subItemRepository.save(findSubItem);
        return mapper.toResponse(findSubItem, SubItemResponse.class);
    }

    // 대체품 삭제
    @Override
    public void deleteSubItem(Long id) throws NotFoundException {
        SubItem subItem = getSubItemOrThrow(id);
        subItem.delete();
        subItemRepository.save(subItem);
    }
}
