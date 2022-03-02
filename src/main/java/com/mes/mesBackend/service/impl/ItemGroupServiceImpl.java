package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ItemGroupRequest;
import com.mes.mesBackend.dto.response.ItemGroupResponse;
import com.mes.mesBackend.entity.ItemGroup;
import com.mes.mesBackend.entity.ModifiedLog;
import com.mes.mesBackend.entity.enumeration.ModifiedDivision;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.ModifiedLogHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ItemGroupRepository;
import com.mes.mesBackend.service.ItemGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mes.mesBackend.entity.enumeration.ModifiedDivision.ITEM_GROUP;

// 품목그룹
@Service
@RequiredArgsConstructor
public class ItemGroupServiceImpl implements ItemGroupService {
    private final ItemGroupRepository itemGroupRepository;
    private final ModelMapper mapper;
    private final ModifiedLogHelper modifiedLogHelper;

    // 품목그룹 생성
    @Override
    public ItemGroupResponse createItemGroup(ItemGroupRequest itemGroupRequest) {
        ItemGroup itemGroup = mapper.toEntity(itemGroupRequest, ItemGroup.class);
        itemGroupRepository.save(itemGroup);
        return mapper.toResponse(itemGroup, ItemGroupResponse.class);
    }

    // 품목그룹 단일 조회
    @Override
    public ItemGroupResponse getItemGroup(Long id) throws NotFoundException {
        ItemGroup itemGroup = getItemGroupOrThrow(id);
        return mapper.toResponse(itemGroup, ItemGroupResponse.class);
    }

    // 품목그룹 리스트 전체 조회
    @Override
    public List<ItemGroupResponse> getItemGroups() {
        List<ItemGroup> itemGroups = itemGroupRepository.findAllByDeleteYnFalse();
        List<ItemGroupResponse> responses = mapper.toListResponses(itemGroups, ItemGroupResponse.class);
        for (ItemGroupResponse r : responses) {
            ModifiedLog modifiedLog = modifiedLogHelper.getModifiedLog(ITEM_GROUP, r.getId());
            if (modifiedLog != null) r.modifiedLog(modifiedLog);
        }
        return responses;
    }

    // 품목그룹 페이징 조회
//    @Override
//    public Page<ItemGroupResponse> getItemGroups(Pageable pageable) {
//        Page<ItemGroup> itemGroups = itemGroupRepository.findAllByDeleteYnFalse(pageable);
//        return mapper.toPageResponses(itemGroups, ItemGroupResponse.class);
//    }

    // 품목그룹 수정
    @Override
    public ItemGroupResponse updateItemGroup(Long id, ItemGroupRequest itemGroupRequest, String userCode) throws NotFoundException {
        ItemGroup findItemGroup = getItemGroupOrThrow(id);
        ItemGroup newItemGroup = mapper.toEntity(itemGroupRequest, ItemGroup.class);
        findItemGroup.put(newItemGroup);
        itemGroupRepository.save(findItemGroup);

        modifiedLogHelper.createModifiedLog(userCode, ITEM_GROUP, findItemGroup);
        return mapper.toResponse(findItemGroup, ItemGroupResponse.class);
    }

    // 품목그룹 삭제
    @Override
    public void deleteItemGroup(Long id) throws NotFoundException {
        ItemGroup itemGroup = getItemGroupOrThrow(id);
        itemGroup.delete();
        itemGroupRepository.save(itemGroup);
    }

    // 품목그룹 조회 및 예외
    @Override
    public ItemGroup getItemGroupOrThrow(Long id) throws NotFoundException {
        return itemGroupRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("itemGroup does not exist. input id: " + id));
    }



//    // 그룹코드 생성
//    @Override
//    public CodeResponse createItemGroupCode(CodeRequest codeRequest) {
//        ItemGroupCode itemGroupCode = mapper.toEntity(codeRequest, ItemGroupCode.class);
//        itemGroupCodeRepository.save(itemGroupCode);
//        return mapper.toResponse(itemGroupCode, CodeResponse.class);
//    }
//
//    // 그룹코드 단일 조회
//    @Override
//    public CodeResponse getItemGroupCode(Long id) throws NotFoundException {
//        return mapper.toResponse(getItemGroupCodeOrThrow(id), CodeResponse.class);
//    }
//
//    // 그룹코드 리스트 조회
//    @Override
//    public List<CodeResponse> getItemGroupCodes() {
//        return mapper.toListResponses(itemGroupCodeRepository.findAll(), CodeResponse.class);
//    }
//
//    // 그룹코드 삭제
//    @Override
//    public void deleteItemGroupCode(Long id) throws NotFoundException, BadRequestException {
//        throwIfItemGroupCodeExist(id);
//        itemGroupCodeRepository.deleteById(id);
//    }
//
//    // 그룹코드 조회 및 예외
//    private ItemGroupCode getItemGroupCodeOrThrow(Long id) throws NotFoundException {
//        return itemGroupCodeRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("itemGroupCode does not exist. input id: " + id));
//    }
//
//    // itemGroupCode 삭제 시 itemGroup에 해당하는 itemGroupCode가 있으면 예외
//    private void throwIfItemGroupCodeExist(Long itemGroupCodeId) throws NotFoundException, BadRequestException {
//        ItemGroupCode itemGroupCode = getItemGroupCodeOrThrow(itemGroupCodeId);
//        boolean existByItemGroupCode = itemGroupRepository.existsByItemGroupCodeAndDeleteYnFalse(itemGroupCode);
//        if (existByItemGroupCode)
//            throw new BadRequestException("code exist in the itemGroup. input code id: " + itemGroupCodeId);
//    }
}
