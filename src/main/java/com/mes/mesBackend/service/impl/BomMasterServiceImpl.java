package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.BomItemRequest;
import com.mes.mesBackend.dto.request.BomMasterRequest;
import com.mes.mesBackend.dto.response.BomItemDetailResponse;
import com.mes.mesBackend.dto.response.BomItemResponse;
import com.mes.mesBackend.dto.response.BomMasterResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.BomItemDetailRepository;
import com.mes.mesBackend.repository.BomMasterRepository;
import com.mes.mesBackend.service.BomMasterService;
import com.mes.mesBackend.service.ClientService;
import com.mes.mesBackend.service.ItemService;
import com.mes.mesBackend.service.WorkProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mes.mesBackend.helper.Constants.DECIMAL_POINT_2;

@Service
@RequiredArgsConstructor
public class BomMasterServiceImpl implements BomMasterService {
    private final BomMasterRepository bomMasterRepository;
    private final ModelMapper mapper;
    private final ItemService itemService;
    private final ClientService clientService;
    private final WorkProcessService workProcessService;
    private final BomItemDetailRepository bomItemDetailRepository;

    // BOM 마스터 생성
    @Override
    public BomMasterResponse createBomMaster(BomMasterRequest bomMasterRequest) throws NotFoundException, BadRequestException {
        Item item = itemService.getItemOrThrow(bomMasterRequest.getItem());

        // 입력받은 item 이 bomMaster 에 이미 등록되어 있는지 채크
        throwIfNotDuplicateItemInBomMasters(item);
        BomMaster bomMaster = mapper.toEntity(bomMasterRequest, BomMaster.class);

        bomMaster.addJoin(item);

        BomMaster save = bomMasterRepository.save(bomMaster);
        return mapper.toResponse(save, BomMasterResponse.class);
    }

    // BOM 마스처 단일 조회
    @Override
    public BomMasterResponse getBomMaster(Long bomMasterId) throws NotFoundException {
        BomMaster bomMaster = getBomMasterOrThrow(bomMasterId);
        return mapper.toResponse(bomMaster, BomMasterResponse.class);
    }

    // BOM 마스처 단일 조회 및 에외
    private BomMaster getBomMasterOrThrow(Long bomMasterId) throws NotFoundException {
        return bomMasterRepository.findByIdAndDeleteYnFalse(bomMasterId)
                .orElseThrow(() -> new NotFoundException("bom master does exist. input id: " + bomMasterId));
    }

    // BOM 마스터 페이징 조회 검색조건: 품목계정, 품목그룹, 품번|품명
    @Override
    public List<BomMasterResponse> getBomMasters(
            Long itemAccountId,
            Long itemGroupId,
            String itemNoAndItemName
    ) {
        List<BomMaster> bomMasters = bomMasterRepository.findAllByCondition(itemAccountId, itemGroupId, itemNoAndItemName);
        return mapper.toListResponses(bomMasters, BomMasterResponse.class);
    }
    // BOM 마스터 페이징 조회 검색조건: 품목계정, 품목그룹, 품번|품명
//    @Override
//    public Page<BomMasterResponse> getBomMasters(
//            Long itemAccountId,
//            Long itemGroupId,
//            String itemNoAndItemName,
//            Pageable pageable
//    ) {
//        Page<BomMaster> bomMasters = bomMasterRepository.findAllByCondition(itemAccountId, itemGroupId, itemNoAndItemName, pageable);
//        return mapper.toPageResponses(bomMasters, BomMasterResponse.class);
//    }

    // BOM 마스터 수정
    @Override
    public BomMasterResponse updateBomMaster(Long bomMasterId, BomMasterRequest bomMasterRequest) throws NotFoundException {
        Item newItem = itemService.getItemOrThrow(bomMasterRequest.getItem());

        BomMaster findBomMaster = getBomMasterOrThrow(bomMasterId);
        BomMaster newBomMaster = mapper.toEntity(bomMasterRequest, BomMaster.class);

        findBomMaster.update(newBomMaster, newItem);

        bomMasterRepository.save(findBomMaster);
        return mapper.toResponse(findBomMaster, BomMasterResponse.class);
    }

    // BOM 마스터 삭제
    @Override
    public void deleteBomMaster(Long bomMasterId) throws NotFoundException {
        BomMaster bomMaster = getBomMasterOrThrow(bomMasterId);
        List<BomItemDetail> bomItemDetails = bomItemDetailRepository.findAllByBomMasterAndDeleteYnFalse(bomMaster);
        bomItemDetails.forEach(BomItemDetail::delete);
        bomItemDetailRepository.saveAll(bomItemDetails);

        bomMaster.delete();
        bomMasterRepository.save(bomMaster);
    }

    // BOM 품목 생성
    @Override
    public BomItemResponse createBomItem(Long bomMasterId, BomItemRequest bomItemRequest) throws NotFoundException {
        BomMaster bomMaster = getBomMasterOrThrow(bomMasterId);

        Client toBuy = clientService.getClientOrThrow(bomItemRequest.getToBuy());
        Item item = itemService.getItemOrThrow(bomItemRequest.getItem());
        WorkProcess workProcess = bomItemRequest.getWorkProcess() != null ?
                workProcessService.getWorkProcessOrThrow(bomItemRequest.getWorkProcess()) : null;
        BomItemDetail bomItemDetail = mapper.toEntity(bomItemRequest, BomItemDetail.class);

        bomItemDetail.addJoin(bomMaster, item, toBuy, workProcess);

        bomItemDetailRepository.save(bomItemDetail);
        return mapper.toResponse(bomItemDetail, BomItemResponse.class);
    }

    // BOM 품목 리스트 조회
    @Override
    public List<BomItemDetailResponse> getBomItems(Long bomMasterId, String itemNoOrItemName) throws NotFoundException {
        BomMaster bomMaster = getBomMasterOrThrow(bomMasterId);
        List<BomItemDetailResponse> responses = bomItemDetailRepository.findAllByCondition(bomMaster.getId(), itemNoOrItemName);
        responses.forEach(m -> m.setPrice(String.format(DECIMAL_POINT_2, m.getAmount() * m.getItemInputUnitPrice())));
        return responses;
    }

    // BOM 품목 수정
    @Override
    public BomItemResponse updateBomItem(Long bomMasterId, Long bomItemId, BomItemRequest bomItemRequest) throws NotFoundException {
        BomItemDetail findBomItemDetail = getBomItemDetailOrThrow(bomMasterId, bomItemId);

        Client newToBuy = clientService.getClientOrThrow(bomItemRequest.getToBuy());
        Item newItem = itemService.getItemOrThrow(bomItemRequest.getItem());
        WorkProcess newWorkProcess = bomItemRequest.getWorkProcess() != null ? workProcessService.getWorkProcessOrThrow(bomItemRequest.getWorkProcess()) : null;

        BomItemDetail newBomItemDetail = mapper.toEntity(bomItemRequest, BomItemDetail.class);

        findBomItemDetail.update(newItem, newToBuy, newWorkProcess, newBomItemDetail);
        bomItemDetailRepository.save(findBomItemDetail);
        return mapper.toResponse(findBomItemDetail, BomItemResponse.class);
    }

    // BOM 품목 삭제
    @Override
    public void deleteBomItem(Long bomMasterId, Long bomItemId) throws NotFoundException {
        BomItemDetail bomItemDetail = getBomItemDetailOrThrow(bomMasterId, bomItemId);
        bomItemDetail.delete();
        bomItemDetailRepository.save(bomItemDetail);
    }

    // BOM 품목 단일 조회
    @Override
    public BomItemResponse getBomItem(Long bomMasterId, Long bomItemDetailId) throws NotFoundException {
        BomItemDetail bomItemDetail = getBomItemDetailOrThrow(bomMasterId, bomItemDetailId);
        return mapper.toResponse(bomItemDetail, BomItemResponse.class);
    }

    // BOM 품목 조회 및 예외
    private BomItemDetail getBomItemDetailOrThrow(Long bomMasterId, Long bomItemDetailId) throws NotFoundException {
        BomMaster bomMaster = getBomMasterOrThrow(bomMasterId);
        return bomItemDetailRepository.findByBomMasterAndIdAndDeleteYnFalse(bomMaster, bomItemDetailId)
                .orElseThrow(() -> new NotFoundException("bomItemDetail does not exist. input id:" + bomItemDetailId));
    }

    // 입력받은 item 이 bomMaster 에 이미 등록되어 있는지 채크
    private void throwIfNotDuplicateItemInBomMasters(Item item) throws BadRequestException {
        boolean b = bomMasterRepository.existsByItemInBomMasters(item.getId());
        if (b) throw new BadRequestException("입략한 품목이 이미 Bom 에 등록되어 있으므로 중복 등록이 불가능 합니다.");
    }
}
