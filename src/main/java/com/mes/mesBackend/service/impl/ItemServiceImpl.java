package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ItemFileRequest;
import com.mes.mesBackend.dto.request.ItemRequest;
import com.mes.mesBackend.dto.response.ItemFileResponse;
import com.mes.mesBackend.dto.response.ItemResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.S3Uploader;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ItemFileRepository;
import com.mes.mesBackend.repository.ItemRepository;
import com.mes.mesBackend.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemAccountService itemAccountService;
    private final ItemGroupService itemGroupService;
    private final ItemFormService itemFormService;
    private final UseTypeService useTypeService;
    private final RoutingService routingService;
    private final UnitService unitService;
    private final LotTypeService lotTypeService;
    private final ClientService clientService;
    private final TestCriteriaService testCriteriaService;
    private final TestProcessService testProcessService;
    private final ItemAccountCodeService itemAccountCodeService;
    private final ItemFileRepository itemFileRepository;
    private final UserService userService;
    private final ModelMapper mapper;
    private final S3Uploader s3Uploader;


    // 품목 생성
    @Override
    public ItemResponse createItem(ItemRequest itemRequest) throws NotFoundException, BadRequestException {
        ItemAccount itemAccount = itemAccountService.getItemAccountOrThrow(itemRequest.getItemAccount());
        ItemAccountCode itemAccountCode = itemAccountCodeService.getItemAccountCodeOrThrow(itemRequest.getItemAccountCode());

        // 입력한 품목계정이 입력받은 품목계정코드의 품목계정이랑 다를 경우 예외
        ifItemAccountDifferentFromItemAccountCodeThrow(itemAccount.getId(), itemAccountCode.getItemAccount().getId());

        ItemGroup itemGroup = itemRequest.getItemGroup() != null ? itemGroupService.getItemGroupOrThrow(itemRequest.getItemGroup()) : null;
        ItemForm itemForm = itemRequest.getItemForm() != null ? itemFormService.getItemFormOrThrow(itemRequest.getItemForm()) : null;
        UseType useType = itemRequest.getUseType() != null ? useTypeService.getUseTypeOrThrow(itemRequest.getUseType()) : null;
        Routing routing = itemRequest.getRouting() != null ? routingService.getRoutingOrThrow(itemRequest.getRouting()) : null;
        Unit unit = unitService.getUnitOrThrow(itemRequest.getUnit());
        LotType lotType = lotTypeService.getLotTypeOrThrow(itemRequest.getLotType());
        Client manufacturer = clientService.getClientOrThrow(itemRequest.getManufacturer());
        TestCriteria testCriteria = itemRequest.getTestCriteria() != null ? testCriteriaService.getTestCriteriaOrThrow(itemRequest.getTestCriteria()) : null;

        Item item = mapper.toEntity(itemRequest, Item.class);

        item.mapping(itemAccount, itemGroup, itemForm, useType, routing, unit, lotType, manufacturer, testCriteria, itemAccountCode);

        itemRepository.save(item);
        return mapper.toResponse(item, ItemResponse.class);
    }

    // 입력한 품목계정이 입력받은 품목계정코드의 품목계정이랑 다를 경우 예외
    private void ifItemAccountDifferentFromItemAccountCodeThrow(Long itemAccountId, Long itemAccountIdFromItemAccountCode) throws BadRequestException {
        if (!itemAccountId.equals(itemAccountIdFromItemAccountCode)) {
            throw new BadRequestException("itemAccount included in the itemAccountCode is different from the itemAccount entered." +
                    " input itemAccount id: " + itemAccountId + ", itemAccount id from itemAccountCode " + itemAccountIdFromItemAccountCode);
        }
    }

    // 품목 단일 조회
    @Override
    public ItemResponse getItem(Long id) throws NotFoundException {
        Item item = getItemOrThrow(id);
        return mapper.toResponse(item, ItemResponse.class);
    }

    // 품목 페이징 조회
    @Override
    public List<ItemResponse> getItems(
            Long itemGroupId,
            Long itemAccountId,
            String itemNo,
            String itemName,
            String searchWord
    ) {
        List<Item> items = itemRepository.findAllByCondition(itemGroupId, itemAccountId, itemNo, itemName, searchWord);
        List<ItemResponse> itemResponses = mapper.toListResponses(items, ItemResponse.class);
        for (ItemResponse res : itemResponses) {
            for (Item item : items) {
                if (res.getId().equals(item.getId())) {
                    res.getItemAccountCode().setItemAccountId(item.getItemAccountCode().getItemAccount().getId());
                    res.getItemAccountCode().setId(item.getItemAccountCode().getId());
                }
            }
        }
        return itemResponses;
    }
    // 품목 페이징 조회
//    @Override
//    public Page<ItemResponse> getItems(
//            Long itemGroupId,
//            Long itemAccountId,
//            String itemNo,
//            String itemName,
//            String searchWord,
//            Pageable pageable
//    ) {
//        Page<Item> items = itemRepository.findAllByCondition(itemGroupId, itemAccountId, itemNo, itemName, searchWord, pageable);
//        return mapper.toPageResponses(items, ItemResponse.class);
//    }

    // 품목 수정
    @Override
    public ItemResponse updateItem(Long id, ItemRequest itemRequest) throws NotFoundException, BadRequestException {
        Item findItem = getItemOrThrow(id);

        ItemAccount newItemAccount = itemAccountService.getItemAccountOrThrow(itemRequest.getItemAccount());
        ItemAccountCode newItemAccountCode = itemAccountCodeService.getItemAccountCodeOrThrow(itemRequest.getItemAccountCode());

        ifItemAccountDifferentFromItemAccountCodeThrow(newItemAccount.getId(), newItemAccountCode.getItemAccount().getId());

        ItemGroup newItemGroup = itemRequest.getItemGroup() != null ? itemGroupService.getItemGroupOrThrow(itemRequest.getItemGroup()) : null;
        ItemForm newItemForm = itemRequest.getItemForm() != null ? itemFormService.getItemFormOrThrow(itemRequest.getItemForm()) : null;
        UseType newUseType = itemRequest.getUseType() != null ? useTypeService.getUseTypeOrThrow(itemRequest.getUseType()) : null;
        Routing newRouting = itemRequest.getRouting() != null ? routingService.getRoutingOrThrow(itemRequest.getRouting()) : null;
        Unit newUnit = unitService.getUnitOrThrow(itemRequest.getUnit());
        LotType newLotType = lotTypeService.getLotTypeOrThrow(itemRequest.getLotType());
        Client newManufacturer = clientService.getClientOrThrow(itemRequest.getManufacturer());
        TestCriteria newTestCriteria = itemRequest.getTestCriteria() != null ? testCriteriaService.getTestCriteriaOrThrow(itemRequest.getTestCriteria()) : null;

        Item newItem = mapper.toEntity(itemRequest, Item.class);

        findItem.update(newItem, newItemAccount, newItemGroup, newItemForm, newUseType, newRouting, newUnit, newLotType, newManufacturer, newTestCriteria, newItemAccountCode);
        itemRepository.save(findItem);
        return mapper.toResponse(findItem, ItemResponse.class);
    }

    // 품목 삭제
    @Override
    public void deleteItem(Long id) throws NotFoundException {
        Item item = getItemOrThrow(id);
        List<ItemFile> itemFiles = itemFileRepository.findAllByItemAndDeleteYnFalse(item);
        for (ItemFile itemFile : itemFiles) {
            itemFile.delete();
        }
        itemFileRepository.saveAll(itemFiles);

        item.delete();
        itemRepository.save(item);
    }

    // 품목 단일 조회 및 예외
    @Override
    public Item getItemOrThrow(Long id) throws NotFoundException {
        return itemRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("item does not exist. input id: " + id));
    }

    // 파일 정보 생성
    @Override
    public ItemFileResponse createItemFileInfo(Long itemId, ItemFileRequest itemFileRequest) throws NotFoundException {
        ItemFile itemFile = mapper.toEntity(itemFileRequest, ItemFile.class);
        Item item = getItemOrThrow(itemId);
        User user = userService.getUserOrThrow(itemFileRequest.getUser());
        itemFile.add(user, item);
        itemFileRepository.save(itemFile);
        return mapper.toResponse(itemFile, ItemFileResponse.class);
    }

    // 파일 생성
    // 파일명: items/품번/item-files/품목파일아이디/파일명(현재날짜)
    @Override
    public ItemFileResponse createFile(Long itemId, Long itemFileId, MultipartFile file) throws IOException, NotFoundException, BadRequestException {
        Item item = checkItemFileToItem(itemId, itemFileId);
        ItemFile itemFile = getItemFileOrThrow(itemFileId);

        String fileName = "items/" + item.getItemNo() + "/" + "item-files/" + itemFile.getId() + "/";
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());

        String uploadFileUrl = s3Uploader.upload(file, fileName);
        itemFile.addFileUrl(uploadFileUrl, fileType);
        itemFileRepository.save(itemFile);
        return mapper.toResponse(itemFile, ItemFileResponse.class);
    }

    // 품목 파일 조회 및 예외
    private ItemFile getItemFileOrThrow(Long id) throws NotFoundException {
        return itemFileRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("fileInfo does not exist. input id: " + id));
    }

    // 조회 된 item이 해당하는 itemFile의 item과 같은지 확인
    private Item checkItemFileToItem(Long itemId, Long itemFileId) throws NotFoundException {
        Item item = getItemOrThrow(itemId);
        ItemFile itemFile = getItemFileOrThrow(itemFileId);
        if (!itemFile.getItem().equals(item)) {
            throw new NotFoundException("itemFile does not exist in the item. input itemId: " + itemId + ", input itemFileId: " + itemFileId);
        }
        return item;
    }

    // 파일 리스트 조회
    @Override
    public List<ItemFileResponse> getItemFiles(Long itemId) throws NotFoundException {
        Item item = getItemOrThrow(itemId);
        List<ItemFile> itemFiles = itemFileRepository.findAllByItemAndDeleteYnFalse(item);
        return mapper.toListResponses(itemFiles, ItemFileResponse.class);
    }

    // 파일 정보 수정
    @Override
    public ItemFileResponse updateItemFileInfo(Long itemId, Long itemFileId, ItemFileRequest itemFileRequest) throws NotFoundException {
        checkItemFileToItem(itemId, itemFileId);
        ItemFile findItemFile = getItemFileOrThrow(itemFileId);
        User user = userService.getUserOrThrow(itemFileRequest.getUser());
        ItemFile newItemFile = mapper.toEntity(itemFileRequest, ItemFile.class);
        findItemFile.update(newItemFile, user);
        return mapper.toResponse(findItemFile, ItemFileResponse.class);
    }

    // 파일 삭제
    @Override
    public void deleteItemFile(Long itemId, Long itemFileId) throws NotFoundException {
        checkItemFileToItem(itemId, itemFileId);
        ItemFile file = getItemFileOrThrow(itemFileId);
        file.delete();
        itemFileRepository.save(file);
    }
}
