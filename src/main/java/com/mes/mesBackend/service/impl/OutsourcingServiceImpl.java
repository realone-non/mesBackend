package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.*;
import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.AmountHelper;
import com.mes.mesBackend.helper.LotHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.OutsourcingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.ItemLogType.STORE_AMOUNT;

@Service
@RequiredArgsConstructor
public class OutsourcingServiceImpl implements OutsourcingService {
    private final ModelMapper modelMapper;
    private final OutSourcingProductionRequestRepository outsourcingProductionRepository;
    private final OutSourcingProductionRawMaterialOutputInfoRepository outsourcingMaterialRepository;
    private final OutsourcingInputRepository outsourcingInputRepository;
    private final OutsourcingReturnRepository outsourcingReturnRepository;
    private final BomMasterRepository bomMasterRepository;
    private final LotMasterRepository lotMasterRepository;
    private final WareHouseRepository wareHouseRepository;
    private final AmountHelper amountHelper;
    private final LotHelper lotHelper;
    private final ItemRepository itemRepository;

    //외주생산의뢰 등록
    @Override
    public OutsourcingProductionResponse createOutsourcingProduction(OutsourcingProductionRequestRequest request) throws NotFoundException, BadRequestException {
        OutSourcingProductionRequest outSourcingProductionRequest = modelMapper.toEntity(request, OutSourcingProductionRequest.class);
        Item item = getItemOrThrow(request.getItemId());

        // 품목이 bomMaster 의 item 에 해당하는지 체크
        throwIfBomMasterExistsItem(item.getId());

        outSourcingProductionRequest.put(item);     // 품목, 생산요청일자
        outsourcingProductionRepository.save(outSourcingProductionRequest);
        return getOutsourcingProductionResponseOrThrow(outSourcingProductionRequest.getId());

//        OutSourcingProductionRequest request = modelMapper.toEntity(outsourcingProductionRequestRequest, OutSourcingProductionRequest.class);
//        request.setBomMaster(bomMasterRepository.getById(outsourcingProductionRequestRequest.getBomId()));
//        request.setProductionDate(LocalDate.now());
//        outsourcingProductionRepository.save(request);
//        return modelMapper.toResponse(request, OutsourcingProductionResponse.class);
    }

    //외주생산의뢰 리스트조회
    public List<OutsourcingProductionResponse> getOutsourcingProductions(Long clientId, String itemNo, String itemName, LocalDate startDate, LocalDate endDate) throws BadRequestException {
        List<OutsourcingProductionResponse> responses = outsourcingProductionRepository.findAllByCondition(clientId, itemNo, itemName, startDate, endDate);
        for (OutsourcingProductionResponse r : responses) {
            Long bomMasterId = bomMasterRepository.findByItemIdAndDeleteYnFalse(r.getItemId()).orElseThrow(() -> new BadRequestException("품목에 해당하는 BOM 정보가 존재하지 않습니다."));
            r.setBomMasterId(bomMasterId);
        }
        return responses;
    }

    //외주생산의뢰 response 단일 조회
    public OutsourcingProductionResponse getOutsourcingProductionResponseOrThrow(Long id) throws NotFoundException, BadRequestException {
        OutsourcingProductionResponse response = outsourcingProductionRepository.findRequestByIdAndDeleteYnAndUseYn(id)
                .orElseThrow(() -> new NotFoundException("해당하는 외주생산의뢰 정보가 존재하지 않습니다."));

        Long bomMasterId = bomMasterRepository.findByItemIdAndDeleteYnFalse(response.getItemId()).orElseThrow(() -> new BadRequestException("품목에 해당하는 BOM 정보가 존재하지 않습니다."));
        response.setBomMasterId(bomMasterId);
        return response;

        //        OutSourcingProductionRequest request = outsourcingProductionRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("not found data"));
//        return modelMapper.toResponse(request, OutsourcingProductionResponse.class);
//        return outsourcingProductionRepository.findRequestByIdAndDeleteYnAndUseYn(id);
    }

    //외주생산의뢰 수정
    public OutsourcingProductionResponse modifyOutsourcingProduction(Long id, OutsourcingProductionRequestRequest outsourcingProduction) throws NotFoundException, BadRequestException {
        OutSourcingProductionRequest findOutsourcingProductionRequest = getOutsourcingProductionRequestOrThrow(id);

        // 외주생산의뢰에 대한 입고정보 존재하면 전부 수정 불가능
        throwIfExistsOutsourcingInputNotUpdate(findOutsourcingProductionRequest.getId());
        // 외주생산의뢰에 해당하는 상세정보 존재하면 품목정보 수정 불가능
        throwIfExistsMaterialNotUpdateItem(findOutsourcingProductionRequest.getId(), findOutsourcingProductionRequest.getItem().getId(), outsourcingProduction.getItemId());

        Item newItem = getItemOrThrow(outsourcingProduction.getItemId());
        OutsourcingProductionRequestRequest newOutsourcingProductionRequestRequest = modelMapper.toEntity(outsourcingProduction, OutsourcingProductionRequestRequest.class);
        findOutsourcingProductionRequest.update(newItem, newOutsourcingProductionRequestRequest);
        outsourcingProductionRepository.save(findOutsourcingProductionRequest);

        return getOutsourcingProductionResponseOrThrow(findOutsourcingProductionRequest.getId());
//        OutSourcingProductionRequest request = outsourcingProductionRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("not found data"));
//        BomMaster bomMaster = bomMasterRepository.getById((outsourcingProduction.getBomId()));
//        request.update(bomMaster, outsourcingProduction);
//        outsourcingProductionRepository.save(request);
//        return modelMapper.toResponse(request, OutsourcingProductionResponse.class);
    }

    //외주생산의뢰 삭제
    public void deleteOutsourcingProduction(Long id) throws NotFoundException, BadRequestException {
        OutSourcingProductionRequest findOutsourcingProductionRequest = getOutsourcingProductionRequestOrThrow(id);

        // 외주생산의뢰에 해당하는 상세정보 존재하면 삭제 불가능
        throwIfExistsMaterialNotDelete(findOutsourcingProductionRequest.getId());
        // 외주생산의뢰에 해당하는 입고정보 존재하면 삭제 불가능
        throwIfExistsOutsourcingInputNotDelete(findOutsourcingProductionRequest.getId());

        findOutsourcingProductionRequest.delete();
        outsourcingProductionRepository.save(findOutsourcingProductionRequest);
//        OutSourcingProductionRequest request = outsourcingProductionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found data"));
//        request.delete();
//        outsourcingProductionRepository.save(request);
    }

    //외주생산 원재료 출고 대상 등록
    public OutsourcingMaterialReleaseResponse createOutsourcingMaterial(Long requestId, OutsourcingMaterialReleaseRequest outsourcingMaterialReleaseRequest) throws NotFoundException, BadRequestException {
        OutSourcingProductionRequest outsourcingProductionRequest = getOutsourcingProductionRequestOrThrow(requestId);

        Item item = getItemOrThrow(outsourcingMaterialReleaseRequest.getItemId());

        // 외주생산의뢰 품목정보에 해당하는 bom 상세 품목정보인지 체크
        throwIfBomItemDetailItemEqItemId(outsourcingProductionRequest.getItem().getId(), item.getId());

        OutSourcingProductionRawMaterialOutputInfo outsourcingMaterial = modelMapper.toEntity(outsourcingMaterialReleaseRequest, OutSourcingProductionRawMaterialOutputInfo.class);
        outsourcingMaterial.put(item, outsourcingProductionRequest);
        outsourcingMaterialRepository.save(outsourcingMaterial);

        return getOutsourcingMaterialResponseOrThrow(outsourcingProductionRequest.getId(), outsourcingMaterial.getId());
//        OutSourcingProductionRawMaterialOutputInfo materialOutputInfo = modelMapper.toEntity(outsourcingMaterialReleaseRequest, OutSourcingProductionRawMaterialOutputInfo.class);
//        materialOutputInfo.setBomItemDetail(bomItemDetailRepository.getById(outsourcingMaterialReleaseRequest.getItemDetailId()));
//        materialOutputInfo.setOutSourcingProductionRequest(outsourcingProductionRepository.getById(id));
//        outsourcingMaterialRepository.save(materialOutputInfo);
//        return outsourcingMaterialRepository.findByMaterialId(materialOutputInfo.getId());
    }

    //외주생산 원재료 출고 대상 리스트 조회
    public List<OutsourcingMaterialReleaseResponse> getOutsourcingMeterials(Long productionId){
        List<OutsourcingMaterialReleaseResponse> responses = outsourcingMaterialRepository.findAllUseYn(productionId);
        responses.forEach(
                m -> m.setAmount(bomMasterRepository.findBomItemDetailByBomMasterItemAndDetailItem(m.getRequestItemId(), m.getItemId()).orElse(0f))
        );
        return responses;
//        return outsourcingMaterialRepository.findAllUseYn(productionId);
    }

    //외주생산 원재료 출고 대상 response 단일 조회
    public OutsourcingMaterialReleaseResponse getOutsourcingMaterialResponseOrThrow(Long requestId, Long materialId) throws NotFoundException {
        OutSourcingProductionRequest request = getOutsourcingProductionRequestOrThrow(requestId);
        OutsourcingMaterialReleaseResponse response = outsourcingMaterialRepository.findByMaterialId(request.getId(), materialId)
                .orElseThrow(() -> new NotFoundException("outsourcinginput not in db:" + materialId));

        Float amount = bomMasterRepository.findBomItemDetailByBomMasterItemAndDetailItem(response.getRequestItemId(), response.getItemId()).orElse(0f);
        response.setAmount(amount);

        return response;
//        OutSourcingProductionRequest request = outsourcingProductionRepository.findByIdAndDeleteYnFalse(requestId).orElseThrow(()-> new NotFoundException("outsourcinginput not in db:" + requestId));
//        return outsourcingMaterialRepository.findByMaterialId(materialId).orElseThrow(()-> new NotFoundException("outsourcinginput not in db:" + materialId));
    }

    //외주생산 원재료 출고 대상 수정
    public OutsourcingMaterialReleaseResponse modifyOutsourcingMaterial(Long requestId, Long materialId, OutsourcingMaterialReleaseRequest request) throws NotFoundException {
        OutSourcingProductionRequest findOutsourcingProductionRequest = getOutsourcingProductionRequestOrThrow(requestId);
        OutSourcingProductionRawMaterialOutputInfo findMaterialInfo = getOutsourcingProductionRawMaterialOutputInfoOrThrow(materialId);

        OutSourcingProductionRawMaterialOutputInfo newMaterialInfo = modelMapper.toEntity(request, OutSourcingProductionRawMaterialOutputInfo.class);
        Item newItem = getItemOrThrow(request.getItemId());

        findMaterialInfo.update(newMaterialInfo, newItem);
        outsourcingMaterialRepository.save(findMaterialInfo);
        return getOutsourcingMaterialResponseOrThrow(findOutsourcingProductionRequest.getId(), findMaterialInfo.getId());
//        OutSourcingProductionRawMaterialOutputInfo info = outsourcingMaterialRepository.findByIdAndDeleteYnFalse(materialId)
//                .orElseThrow(() -> new IllegalArgumentException("not found data"));
//        BomItemDetail itemDetail = bomItemDetailRepository.getById(request.getItemDetailId());
//        info.update(request, itemDetail);
//        outsourcingMaterialRepository.save(info);
//        return outsourcingMaterialRepository.findByMaterialId(materialId).orElseThrow(()-> new NotFoundException("outsourcinginput not in db:" + materialId));
    }

    //외주생산 원재료 출고 대상 삭제
    public void deleteOutsourcingMaterial(Long requestId, Long id) throws NotFoundException {
        OutSourcingProductionRawMaterialOutputInfo findMaterialInfo = getOutsourcingProductionRawMaterialOutputInfoOrThrow(id);
        findMaterialInfo.delete();
        outsourcingMaterialRepository.save(findMaterialInfo);

//        OutSourcingProductionRequest request = outsourcingProductionRepository.findByIdAndDeleteYnFalse(requestId).orElseThrow(() -> new IllegalArgumentException("not found data"));
//        OutSourcingProductionRawMaterialOutputInfo info = outsourcingMaterialRepository.findByIdAndDeleteYnFalse(id)
//                .orElseThrow(() -> new IllegalArgumentException("not found data"));
//        info.delete();
//        outsourcingMaterialRepository.save(info);
    }

//    //외주 입고정보 등록
//    public OutsourcingInputResponse createOutsourcingInput(OutsourcingInputRequest request) throws NotFoundException {
//        OutSourcingInput input = modelMapper.toEntity(request, OutSourcingInput.class);
//        input.setProductionRequest(outsourcingProductionRepository.findByIdAndDeleteYnFalse(request.getRequestId()).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + request.getRequestId())));
//        input.setInputWareHouse(wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId()).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + request.getWarehouseId())));
//        outsourcingInputRepository.save(input);
//        return outsourcingInputRepository.findInputByIdAndDeleteYnAndUseYn(input.getId()).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + input.getId()));
//    }

    //외주 입고정보 리스트조회
    public List<OutsourcingInputResponse> getOutsourcingInputList(Long clientId, String itemNo, String itemName, LocalDate startDate, LocalDate endDate){
        List<OutsourcingInputResponse> responses = outsourcingInputRepository.findAllByCondition(clientId, itemNo, itemName, startDate, endDate);

        for (OutsourcingInputResponse r : responses) {
            List<OutSourcingInput> inputs = outsourcingInputRepository.findOutsourcingInputByRequestId(r.getId());
            // 총 입고수량
            int allInputAmount = inputs.stream().mapToInt(OutSourcingInput::getInputAmount).sum();
            r.setInputAmount(allInputAmount);

            // 미입고수량
            r.setNoInputAmount(r.getProductionAmount() - allInputAmount);

            // 창고
            String warehouseName = inputs.stream().map(m -> m.getInputWareHouse().getWareHouseName()).findFirst().orElse(null);
            r.setWarehouseName(warehouseName);

            // 입고일시
            LocalDate inputDate = inputs.stream().map(OutSourcingInput::getInputDate).findFirst().orElse(null);
            r.setInputDate(inputDate);

            // 수입검사여부
            Boolean inputTestYn = inputs.stream().map(OutSourcingInput::isInputTestYn).findFirst().orElse(null);
            r.setInputTestYn(inputTestYn);
        }

        return responses;
//        for (OutsourcingInputResponse response : allByCondition) {
//            List<OutSourcingInput> allByRequestId = outsourcingInputRepository.findAllByRequestId(response.getId());
//            if(allByRequestId.size() > 0){
//                response.setInputDate(allByRequestId.get(0).getInputDate());
//                response.setInputAmount(allByRequestId.stream().mapToInt(OutSourcingInput::getInputAmount).sum());
//                if(allByRequestId.get(0).getInputWareHouse() != null){
//                    response.setWarehouseName(allByRequestId.get(0).getInputWareHouse().getWareHouseName());
//                }
//                else{
//                    response.setWarehouseName("");
//                }
//            }
//            Integer amount = outsourcingInputRepository.findAmountByRequestId(response.getId());
//            response.setInputTestYn(allByRequestId.get(0).get);
//            response.setNoInputAmount(amount - response.getInputAmount());
//        }
        //.stream().map(OutsourcingInputResponse::setOutSourcingInputTestItemName).collect(Collectors.toList());
//        return allByCondition;
    }

//    //외주 입고정보 수정
//    public OutsourcingInputResponse modifyOutsourcingInput(Long inputId, OutsourcingInputRequest request) throws NotFoundException {
//        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(inputId).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + inputId));
//        OutSourcingProductionRequest prodRequest = outsourcingProductionRepository.findByIdAndDeleteYnFalse(request.getRequestId()).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + inputId));
//        WareHouse wareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId()).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + inputId));
//        input.update(request, prodRequest, wareHouse);
//        outsourcingInputRepository.save(input);
//        return outsourcingInputRepository.findInputByIdAndDeleteYnAndUseYn(inputId).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + inputId));
//    }

//    //외주 입고정보 삭제
//    public void deleteOutsourcingInput(Long requestId) throws NotFoundException {
//        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(requestId).orElseThrow(()-> new NotFoundException("outsourcingInfo not in db:" + requestId));;
//        input.delete();
//        outsourcingInputRepository.save(input);
//    }

    //외주 입고 LOT정보 등록
    public OutsourcingInputLOTResponse createOutsourcingInputLOT(Long requestId, OutsourcingInputLOTRequest request) throws NotFoundException, BadRequestException {
        OutSourcingProductionRequest findRequest = getOutsourcingProductionRequestOrThrow(requestId);

        // 입고된 수량이 요청수량보다 많은지 체크
        throwIfInputAmountCheckCreate(requestId, request.getInputAmount(), findRequest.getProductionAmount());

        WareHouse warehouse = getWarehouseOrThrow(request.getWarehouseId());

        OutSourcingInput outsourcingInput = modelMapper.toEntity(request, OutSourcingInput.class);
        outsourcingInput.put(findRequest, warehouse);
        outsourcingInputRepository.save(outsourcingInput);

        // 수입검사여부 true 일 경우 재고수량 0, false 일 경우 입고수량
        int stockAmount = request.isInputTestYn() ? 0 : request.getInputAmount();

        // lot 생성
        LotMasterRequest lotMasterRequest = new LotMasterRequest();
        lotMasterRequest.putOutsourcingInputLotRequest(
                findRequest.getItem(),
                warehouse,
                outsourcingInput,
                stockAmount,
                request.getInputAmount()
        );
        LotMaster lotMaster = lotHelper.createLotMaster(lotMasterRequest);

        amountHelper.amountUpdate(lotMaster.getItem().getId(), outsourcingInput.getInputWareHouse().getId(), null, STORE_AMOUNT, request.getInputAmount(), true);

        return getOutsourcingInputLOTResponseOrThrow(findRequest.getId(), outsourcingInput.getId());
//        OutSourcingProductionRequest productionRequest = outsourcingProductionRepository.findByIdAndDeleteYnFalse(requestId)
//                .orElseThrow(() -> new NotFoundException("외주생산의뢰가 존재하지 않습니다. id: " + requestId));
//        List<OutSourcingInput> allByRequestId = outsourcingInputRepository.findAllByRequestId(requestId);
//        OutSourcingInput input = new OutSourcingInput();
//        WareHouse wareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId())
//                .orElseThrow(() -> new NotFoundException("해당 창고가 존재하지 않습니다. id: " + requestId));
//        //외주입고 생성
//        //이미 외주입고가 존재 할 경우, 기존 정보와 합침
//        if(allByRequestId.size() > 0){
//            input.setProductionRequest(productionRequest);
//            input.setInputDate(LocalDate.now());
//            input.setInputAmount(allByRequestId.stream().mapToInt(OutSourcingInput::getInputAmount).sum() + request.getInputAmount());
//            input.setInputWareHouse(wareHouse);
//            input.setInputTestYn(request.isInputTestYn());
//            input.setTestRequestType(request.getRequestTestType());
//        }
//        //외주입고 정보가 존재 하지 않을 경우, 새롭게 생성
//        else{
//            input.setProductionRequest(productionRequest);
//            input.setInputDate(LocalDate.now());
//            input.setInputAmount(request.getInputAmount());
//            input.setInputTestYn(request.isInputTestYn());
//            input.setInputWareHouse(wareHouse);
//        }
//
//        outsourcingInputRepository.save(input);
//
//        //Lot 생성
//        LotMasterRequest lotMasterRequest = new LotMasterRequest();
        //수입검사여부가 true일 경우 재고 수량 0, 아닐 경우 생성 수량과 동일하게
//        if(input.getProductionRequest().isInputTestYn() == true){
//            lotMasterRequest.putOutsourcingInputLotRequest(
//                    input.getProductionRequest().getBomMaster().getItem(),
//                    input.getInputWareHouse(),
//                    input,
//                    0,
//                    request.getInputAmount(),
//                    input.getProductionRequest().getBomMaster().getItem().getLotType()
//            );
//        }
//        else{
//            lotMasterRequest.putOutsourcingInputLotRequest(
//                    input.getProductionRequest().getBomMaster().getItem(),
//                    input.getInputWareHouse(),
//                    input,
//                    request.getInputAmount(),
//                    request.getInputAmount(),
//                    input.getProductionRequest().getBomMaster().getItem().getLotType()
//            );
//        }

        //LOT번호 생성
//        String lotNo = lotHelper.createLotMaster(lotMasterRequest).getLotNo();
//
//        LotMaster lotMaster = lotMasterRepository.findByLotNoAndDeleteYnFalse(lotNo)
//                .orElseThrow(()-> new NotFoundException("해당 LOT번호가 존재하지 않습니다." + lotNo));
//
//        amountHelper.amountUpdate(lotMaster.getItem().getId(), input.getInputWareHouse().getId(), null,   ItemLogType.STORE_AMOUNT, request.getInputAmount(), true);
//
//        OutsourcingInputLOTResponse lotResponse = new OutsourcingInputLOTResponse();
//
//        lotResponse.setLotNo(lotNo);
//        lotResponse.setLotId(lotMaster.getId());
//        lotResponse.setId(lotMaster.getOutSourcingInput().getId());
//        lotResponse.setLotType(lotMaster.getLotType().getLotType());
//        lotResponse.setInputAmount(lotMaster.getCreatedAmount());
//        lotResponse.setTestRequestType(lotMaster.getOutSourcingInput().getTestRequestType());

//        return lotResponse;
    }

    //외주 입고 LOT정보 리스트조회
    public List<OutsourcingInputLOTResponse> getOutsourcingInputLOTList(Long requestId) {
//        List<OutSourcingInput> inputList = outsourcingInputRepository.findAllByRequestId(requestId);
//        List<OutsourcingInputLOTResponse> lotList = new ArrayList<>();
//        for (OutSourcingInput outSourcingInput : inputList) {
//            LotMaster lotMaster = lotMasterRepository.findByOutsourcingInput(outSourcingInput.getId());
//            if(lotMaster == null){
//                break;
//            }
//            OutsourcingInputLOTResponse response = new OutsourcingInputLOTResponse();
//            response.setId(outSourcingInput.getId());
//            response.setLotId(lotMaster.getId());
//            response.setLotType(lotMaster.getLotType().getLotType());
//            response.setLotNo(lotMaster.getLotNo());
//            response.setInputAmount(lotMaster.getCreatedAmount());
//            response.setTestRequestType(outSourcingInput.getTestRequestType());
//            response.setInputTestYn(outSourcingInput.isInputTestYn());
//            response.setWarehouseId(outSourcingInput.getInputWareHouse().getId());
//            response.setWarehouseName(outSourcingInput.getInputWareHouse().getWareHouseName());
//            lotList.add(response);
//        }
//        return lotList;
        return outsourcingInputRepository.findOutsourcingInputLotResponsesByRequestId(requestId);
    }

    //외주 입고 LOT정보 조회
    public OutsourcingInputLOTResponse getOutsourcingInputLOTResponseOrThrow(Long requestId, Long inputId) throws NotFoundException {
        OutSourcingProductionRequest outsourcingRequest = getOutsourcingProductionRequestOrThrow(requestId);
        return outsourcingInputRepository.findOutsourcingInputLotResponseByRequestId(outsourcingRequest.getId(), inputId)
                .orElseThrow(() -> new NotFoundException("외주입고 lot 정보가 존재하지 않습니다."));
//        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(inputId).orElseThrow(()-> new NotFoundException("해당 입고 정보가 존재하지 않습니다:" + inputId));;
//        LotMaster lotMaster = lotMasterRepository.findByOutsourcingInput(inputId);
//        if(lotMaster == null){
//            throw new BadRequestException("해당 LOT정보가 존재하지 않습니다.");
//        }
//        OutsourcingInputLOTResponse response = new OutsourcingInputLOTResponse();
//        response.setId(input.getId());
//        response.setLotId(lotMaster.getId());
//        response.setLotType(lotMaster.getLotType().getLotType());
//        response.setLotNo(lotMaster.getLotNo());
//        response.setInputAmount(lotMaster.getCreatedAmount());
//        response.setTestRequestType(input.getTestRequestType());
//        response.setInputTestYn(input.isInputTestYn());
//        response.setWarehouseId(input.getInputWareHouse().getId());
//        response.setWarehouseName(input.getInputWareHouse().getWareHouseName());
//
//        return response;
    }

    //외주 입고 LOT정보 수정
    public OutsourcingInputLOTResponse modifyOutsourcingInputLOT(Long requestId, Long inputId, OutsourcingInputLOTRequest request) throws NotFoundException, BadRequestException {
        OutSourcingProductionRequest outsourcingRequest = getOutsourcingProductionRequestOrThrow(requestId);
        OutSourcingInput findOutsourcingInput = getOutsourcingInputOrThrow(inputId);
        LotMaster findLotmaster = getInputLotMasterOrThrow(findOutsourcingInput.getId());

        // 해당 Lot 사용된거면 수정 불가능
        throwIfInputAmountNotZero(findLotmaster.getInputAmount());
        // 해당 Lot 가 외주수입검사에 등록되어있으면 InputTestYn 수정 불가능
        throwIfLotMasterCheckRequestAmountNotZeroUpdate(findLotmaster.getCheckRequestAmount(), findOutsourcingInput.isInputTestYn(), request.isInputTestYn());
        // 입고된 수량이 요청수량보다 많은지 체크
        throwIfInputAmountCheckUpdate(outsourcingRequest.getId(), request.getInputAmount(), outsourcingRequest.getProductionAmount(), findOutsourcingInput.getInputAmount());

        WareHouse newWarehouse = getWarehouseOrThrow(request.getWarehouseId());

        // 수정 반영
        OutSourcingInput newOutsourcingInput = modelMapper.toEntity(request, OutSourcingInput.class);
        findOutsourcingInput.update(newOutsourcingInput, newWarehouse);
        outsourcingInputRepository.save(findOutsourcingInput);

        // 해당 lot 재고수량, 생성수량 변경
        int stockAmount = request.isInputTestYn() ? 0 : request.getInputAmount();
        findLotmaster.updateOutsourcingInput(findOutsourcingInput.getInputAmount(), stockAmount);
        lotMasterRepository.save(findLotmaster);

        return getOutsourcingInputLOTResponseOrThrow(outsourcingRequest.getId(), findOutsourcingInput.getId());

//        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(inputId).orElseThrow(()-> new NotFoundException("해당 입고 정보가 존재하지 않습니다:" + inputId));
//        WareHouse wareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId()).orElseThrow(()-> new NotFoundException("해당 창고가 존재하지 않습니다:" + inputId));
//        LotMaster lotInfo = lotMasterRepository.findByOutsourcingInput(inputId);
//        if(lotInfo == null){
//            throw new BadRequestException("해당 입고 LOT정보가 존재하지 않습니다.");
//        }
//        if(input.isInputTestYn() == true){
//            if(lotInfo.getStockAmount() > 0){
//                new BadRequestException("수입검사가 진행중이거나 완료된 입고 건은 수정 할 수 없습니다.");
//            }
//            input.setInputAmount(input.getInputAmount() + (request.getInputAmount() - lotInfo.getCreatedAmount()));
//            lotInfo.setCreatedAmount(request.getInputAmount());
//            lotInfo.setLotType(input.getProductionRequest().getBomMaster().getItem().getLotType());
//            lotInfo.setWareHouse(wareHouse);
//            lotMasterRepository.save(lotInfo);
//        }
//        else{
//            if(lotInfo.getCreatedAmount() != lotInfo.getStockAmount()){
//                new BadRequestException("재고수량과 입고수량이 다를 경우 수정 할 수 없습니다.");
//            }
//            input.setInputAmount(input.getInputAmount() + (request.getInputAmount() - lotInfo.getCreatedAmount()));
//            lotInfo.setCreatedAmount(request.getInputAmount());
//            lotInfo.setStockAmount(request.getInputAmount());
//            lotInfo.setLotType(input.getProductionRequest().getBomMaster().getItem().getLotType());
//            lotInfo.setWareHouse(wareHouse);
//            lotMasterRepository.save(lotInfo);
//        }
//        return inputId;
    }

    //외주 입고 LOT정보 삭제
    public void deleteOutsourcingInputLOT(Long requestId, Long inputId) throws NotFoundException, BadRequestException {
        OutSourcingInput findOutsourcingInput = getOutsourcingInputOrThrow(inputId);
        LotMaster findLotmaster = getInputLotMasterOrThrow(findOutsourcingInput.getId());

        // 외주수입검사의뢰에 등록되어있는지 체크
        throwIfLotMasterCheckRequestAmountNotZeroDelete(findLotmaster.getCheckRequestAmount());
        // 사용된 lot 인지 체크
        throwIfInputAmountNotZero(findLotmaster.getInputAmount());

        // 외주입고 삭제
        findOutsourcingInput.delete();
        // lot 삭제
        findLotmaster.delete();

        outsourcingInputRepository.save(findOutsourcingInput);
        lotMasterRepository.save(findLotmaster);
//        LotMaster lotInfo = lotMasterRepository.findByOutsourcingInput(inputId);
//        if(lotInfo == null){
//            throw new BadRequestException("해당 LOT정보가 존재하지 않습니다.");
//        }
//
//        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(inputId).orElseThrow(()-> new NotFoundException("외주입고정보가 존재하지 않습니다."));
//        input.setInputAmount(input.getInputAmount() - lotInfo.getCreatedAmount());
//        input.setDeleteYn(true);
//        outsourcingInputRepository.save(input);
//        lotInfo.delete();
//        lotMasterRepository.save(lotInfo);
    }

    //외주 반품 등록
    public OutsourcingReturnResponse createOutsourcingReturn(OutsourcingReturnRequest request) throws NotFoundException, BadRequestException {
        OutsourcingReturn returning = modelMapper.toEntity(request, OutsourcingReturn.class);
        LotMaster lotMaster = lotMasterRepository.findByIdAndDeleteYnFalse(request.getLotMasterId()).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + request.getLotMasterId()));
        if(request.isReturnDivision() == true && lotMaster.getStockAmount() >= request.getStockReturnAmount() == true){
                 lotMaster.setStockAmount(lotMaster.getStockAmount() - request.getStockReturnAmount());
                 lotMaster.setStockReturnAmount(request.getStockReturnAmount());
        }
        else if(request.isReturnDivision() == false && lotMaster.getBadItemAmount() >= request.getBadItemReturnAmount() == true){
            lotMaster.setBadItemAmount(lotMaster.getBadItemAmount() - request.getBadItemReturnAmount());
            lotMaster.setBadItemReturnAmount(request.getBadItemReturnAmount());
        }
        else{
            throw new BadRequestException("returnAmount can not bigger than stockAmount or badItemAmount");
        }
        returning.setLotMaster(lotMaster);
        outsourcingReturnRepository.save(returning);
        return outsourcingReturnRepository.findReturnByIdAndDeleteYnAndUseYn(returning.getId()).orElseThrow(()-> new NotFoundException("returnInfo not in db:" + returning.getId()));
    }

    //외주 반품 리스트조회
    public List<OutsourcingReturnResponse> getOutsourcingReturnList(Long clientId, String itemNo, String itemName, LocalDate startDate, LocalDate endDate){
        return outsourcingReturnRepository.findAllByCondition(clientId, itemNo, itemName, startDate, endDate);
    }

    //외주 반품 조회
    public OutsourcingReturnResponse getOutsourcingReturn(Long returnId) throws NotFoundException {
        return outsourcingReturnRepository.findReturnByIdAndDeleteYnAndUseYn(returnId).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + returnId));
    }

    //외주 반품 수정
    public OutsourcingReturnResponse modifyOutsourcingReturn(Long returnId, OutsourcingReturnRequest request) throws NotFoundException, BadRequestException {
        OutsourcingReturn returning = outsourcingReturnRepository.findByIdAndDeleteYnFalse(returnId).orElseThrow(()-> new NotFoundException("returnInfo not in db:" + returnId));
        LotMaster lotMaster = lotMasterRepository.findByIdAndDeleteYnFalse(request.getLotMasterId()).orElseThrow(()-> new NotFoundException("lotInfo not in db:" + request.getLotMasterId()));
        if(request.isReturnDivision() == true && (lotMaster.getStockAmount() + lotMaster.getStockReturnAmount()) == (request.getStockAmount() + request.getStockReturnAmount())){
            lotMaster.setStockAmount(request.getStockAmount());
            lotMaster.setStockReturnAmount(request.getStockReturnAmount());
        }
        else if(request.isReturnDivision() == false && (lotMaster.getBadItemAmount() + lotMaster.getBadItemReturnAmount()) == (request.getBadItemAmount()) + request.getBadItemReturnAmount()){
            lotMaster.setBadItemAmount(request.getBadItemAmount());
            lotMaster.setBadItemReturnAmount(request.getBadItemReturnAmount());
        }
        else{
            if(request.isReturnDivision() == true){
                throw new BadRequestException("반품수량은 재고수량을 초과할 수 없습니다. 재고수량 :" + lotMaster.getStockAmount());
            }
            else if(request.isReturnDivision() == false){
                throw new BadRequestException("반품수량은 불량수량을 초과할 수 없습니다. 불량수량 :" + lotMaster.getBadItemAmount());
            }
        }
        returning.update(request, lotMaster);
        outsourcingReturnRepository.save(returning);
        return outsourcingReturnRepository.findReturnByIdAndDeleteYnAndUseYn(returnId).orElseThrow(()-> new NotFoundException("returninfo not in db:" + returnId));
    }

    //외주 입고정보 삭제
    public void deleteOutsourcingReturn(Long id) throws NotFoundException {
        OutsourcingReturn returning = outsourcingReturnRepository.findByIdAndDeleteYnFalse(id).orElseThrow(()-> new NotFoundException("outsourcingInfo not in db:" + id));
        returning.delete(returning);
        outsourcingReturnRepository.save(returning);
    }

    //외주 현황 조회
    public List<OutsourcingStatusResponse> getOutsourcingStatusList(Long clientId, String itemNo, String itemName){
        return outsourcingInputRepository.findStatusByCondition(clientId, itemNo, itemName);
    }

    //외주 입고정보 조회
    public OutsourcingInputResponse getOutsourcingInputResponseOrThrow(Long inputId) throws NotFoundException {
        return outsourcingInputRepository.findInputByIdAndDeleteYnAndUseYn(inputId)
                .orElseThrow(()-> new NotFoundException("lotinfo not in db:" + inputId));
    }

    // 외주입고 단일 조회 및 예외
    private OutSourcingInput getOutsourcingInputOrThrow(Long id) throws NotFoundException {
        return outsourcingInputRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("외주입고 정보가 존재하지 않습니다. input id: " + id));
    }

    // 외주생산 원재료 출고대상 정보 단일 조회
    private OutSourcingProductionRawMaterialOutputInfo getOutsourcingProductionRawMaterialOutputInfoOrThrow(Long id) throws NotFoundException {
        return outsourcingMaterialRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("외주생산 원재료 출고 정보가 존재하지 않습니다."));
    }

    // 창고 단일 조회 및 예외
    private WareHouse getWarehouseOrThrow(Long id) throws NotFoundException {
        return wareHouseRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("warehouse does not exists. input id: " + id));
    }

    // 품목 단일 조회 및 예외
    private Item getItemOrThrow(Long id) throws NotFoundException {
        return itemRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("item does not exist. input item id: " + id));
    }

    // 외주생산의뢰 단일 조회
    private OutSourcingProductionRequest getOutsourcingProductionRequestOrThrow(Long id) throws NotFoundException {
        return outsourcingProductionRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("해당하는 외주생산의뢰 정보가 존재하지 않습니다."));
    }

    // 외주생산의뢰에 해당하는 상세정보 존재하면 품목정보 수정 불가능
    private void throwIfExistsMaterialNotUpdateItem(Long requestId, Long beforeItemId, Long newItemId) throws BadRequestException {
        if (!beforeItemId.equals(newItemId)) {
            boolean b = outsourcingMaterialRepository.existsByRequestId(requestId);
            if (b) throw new BadRequestException("상세정보가 등록되어 있으므로 품목정보 수정이 불가능 합니다.");
        }
    }

    // 외주생산의뢰에 대한 입고정보 존재하면 전부 수정 불가능
    private void throwIfExistsOutsourcingInputNotUpdate(Long requestId) throws BadRequestException {
        boolean b = outsourcingInputRepository.existsByOutsourcingProductionRequestId(requestId);
        if (b) throw new BadRequestException("입고가 진행된 정보이므로 수정이나 삭제가 불가능합니다.");
    }

    // 외주생산의뢰에 해당하는 상세정보 존재하면 삭제 불가능
    private void throwIfExistsMaterialNotDelete(Long requestId) throws BadRequestException {
        boolean b = outsourcingMaterialRepository.existsByRequestId(requestId);
        if (b) throw new BadRequestException("상세정보가 등록되어 있으므로 삭제가 불가능합니다. 상세정보 삭제 후 다시 시도해주세요.");
    }

    // 외주생산의뢰에 해당하는 입고정보 존재하면 삭제 불가능
    private void throwIfExistsOutsourcingInputNotDelete(Long requestId) throws BadRequestException {
        boolean b = outsourcingInputRepository.existsByOutsourcingProductionRequestId(requestId);
        if (b) throw new BadRequestException("입고가 진행된 정보이므로 수정이나 삭제가 불가능합니다.");
    }

    // 외주입고 id 로 lotMaster 조회
    private LotMaster getInputLotMasterOrThrow(Long outsourcingInputId) throws NotFoundException {
        return lotMasterRepository.findByOutsourcingInput(outsourcingInputId)
                .orElseThrow(() -> new NotFoundException("해당하는 외주입고 lot 가 존재하지 않습니다."));
    }

    // 해당 Lot 사용된거면 수정, 삭제 불가능
    private void throwIfInputAmountNotZero(int inputAmount) throws BadRequestException {
        if (inputAmount != 0) throw new BadRequestException("공정에 투입된 lot 이므로 수정이나 삭제가 불가능합니다.");
    }

    // 외주수입검사에 등록 되어있는지 체크
    private void throwIfLotMasterCheckRequestAmountNotZeroUpdate(int checkRequestAmount, boolean findInputTestYn, boolean newInputTestYn) throws BadRequestException {
        if (findInputTestYn != newInputTestYn) {
            if (checkRequestAmount != 0) throw new BadRequestException("해당 외주입고는 외주수입검사의뢰에 등록 되어있으므로 수입검사여부 수정 불가능합니다.");
        }
    }

    // 외주수입검사의뢰에 등록되어있는지 체크
    private void throwIfLotMasterCheckRequestAmountNotZeroDelete(int checkRequestAmount) throws BadRequestException {
        if (checkRequestAmount != 0) {
            throw new BadRequestException("해당 외주입고는 외주수입검사의뢰에 등록 되어있으므로 삭제가 불가능합니다.");
        }
    }

    // 외주생산의뢰 품목정보에 해당하는 bom 상세 품목정보인지 체크
    private void throwIfBomItemDetailItemEqItemId(Long bomMasterItemId, Long bomDetailItemId) throws BadRequestException {
        boolean b = bomMasterRepository.existsBomItemDetailByItemId(bomMasterItemId, bomDetailItemId);
        if (!b) throw new BadRequestException("해당 품목은 외주생산의뢰한 품목에 해당하는 bom 상세 품목정보가 아닙니다. 확인 후 다시 시도해주세요.");
    }

    // 해당 품목이 bom 에 등록되어 있는지 여부
    private void throwIfBomMasterExistsItem(Long itemId) throws BadRequestException {
        boolean b = bomMasterRepository.existsByItemInBomMasters(itemId);
        if (!b) throw new BadRequestException("입력한 품목정보는 BOM 에 등록되어 있지 않습니다. 확인 후 다시 시도해주세요.");
    }


    // 입고된 수량이 요청수량보다 많은지 체크
    private void throwIfInputAmountCheckCreate(Long requestId, int inputAmount, int requestAmount) throws BadRequestException {
        int allInputAmount = outsourcingInputRepository.findAllByRequestId(requestId)
                .stream().mapToInt(OutSourcingInput::getInputAmount).sum();
        if ((allInputAmount + inputAmount) > requestAmount) {
            throw new BadRequestException("생산요청수량보다 입고 수량이 많을 수 없습니다. 확인 후 다시 시도해주세요.");
        }
    }

    // 입고된 수량이 요청수량보다 많은지 체크
    private void throwIfInputAmountCheckUpdate(Long requestId, int inputAmount, int requestAmount, int beforeInputAmount) throws BadRequestException {
        int allInputAmount = outsourcingInputRepository.findAllByRequestId(requestId)
                .stream().mapToInt(OutSourcingInput::getInputAmount).sum();
        if (((allInputAmount - beforeInputAmount) + inputAmount) > requestAmount) {
            throw new BadRequestException("생산요청수량보다 입고 수량이 많을 수 없습니다. 확인 후 다시 시도해주세요.");
        }
    }
}
