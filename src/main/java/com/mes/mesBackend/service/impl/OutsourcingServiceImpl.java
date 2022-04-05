package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.*;
import com.mes.mesBackend.dto.response.*;

import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.ItemLogType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.AmountHelper;
import com.mes.mesBackend.helper.LotHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.OutsourcingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OutsourcingServiceImpl implements OutsourcingService {
    private final ModelMapper modelMapper;
    private final OutSourcingProductionRequestRepository outsourcingProductionRepository;
    private final OutSourcingProductionRawMaterialOutputInfoRepository outsourcingMaterialRepository;
    private final OutsourcingInputRepository outsourcingInputRepository;
    private final OutsourcingReturnRepository outsourcingReturnRepository;
    private final BomMasterRepository bomMasterRepository;
    private final BomItemDetailRepository bomItemDetailRepository;
    private final LotMasterRepository lotMasterRepository;
    private final WareHouseRepository wareHouseRepository;
    private final LotTypeRepository lotTypeRepository;
    private final AmountHelper amountHelper;
    private final LotHelper lotHelper;


    //외주생산의뢰 등록
    @Override
    public OutsourcingProductionResponse createOutsourcingProduction(OutsourcingProductionRequestRequest outsourcingProductionRequestRequest)  {
        OutSourcingProductionRequest request = modelMapper.toEntity(outsourcingProductionRequestRequest, OutSourcingProductionRequest.class);
        request.setBomMaster(bomMasterRepository.getById(outsourcingProductionRequestRequest.getBomId()));
        request.setProductionDate(LocalDate.now());
        outsourcingProductionRepository.save(request);
        return modelMapper.toResponse(request, OutsourcingProductionResponse.class);
    }

    //외주생산의뢰 리스트조회
    public List<OutsourcingProductionResponse> getOutsourcingProductions(Long clientId, String itemNo, String itemName, LocalDate startDate, LocalDate endDate){
        return outsourcingProductionRepository.findAllByCondition(clientId, itemNo, itemName, startDate, endDate);
    }

    //외주생산의뢰 조회
    public Optional<OutsourcingProductionResponse> getOutsourcingProduction(Long id){
//        OutSourcingProductionRequest request = outsourcingProductionRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("not found data"));
//        return modelMapper.toResponse(request, OutsourcingProductionResponse.class);
        return outsourcingProductionRepository.findRequestByIdAndDeleteYnAndUseYn(id);
    }

    //외주생산의뢰 수정
    public OutsourcingProductionResponse modifyOutsourcingProduction(Long id, OutsourcingProductionRequestRequest outsourcingProduction){
        OutSourcingProductionRequest request = outsourcingProductionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found data"));
        BomMaster bomMaster = bomMasterRepository.getById((outsourcingProduction.getBomId()));
        request.update(bomMaster, outsourcingProduction);
        outsourcingProductionRepository.save(request);
        return modelMapper.toResponse(request, OutsourcingProductionResponse.class);
    }

    //외주생산의뢰 삭제
    public void deleteOutsourcingProduction(Long id){
        OutSourcingProductionRequest request = outsourcingProductionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found data"));
        request.delete();
        outsourcingProductionRepository.save(request);
    }

    //외주생산 원재료 출고 대상 등록
    public Optional<OutsourcingMaterialReleaseResponse> createOutsourcingMaterial(Long id, OutsourcingMaterialReleaseRequest outsourcingMaterialReleaseRequest){
        OutSourcingProductionRawMaterialOutputInfo materialOutputInfo = modelMapper.toEntity(outsourcingMaterialReleaseRequest, OutSourcingProductionRawMaterialOutputInfo.class);
        materialOutputInfo.setBomItemDetail(bomItemDetailRepository.getById(outsourcingMaterialReleaseRequest.getItemDetailId()));
        materialOutputInfo.setOutSourcingProductionRequest(outsourcingProductionRepository.getById(id));
        outsourcingMaterialRepository.save(materialOutputInfo);
        return outsourcingMaterialRepository.findByMaterialId(materialOutputInfo.getId());
    }

    //외주생산 원재료 출고 대상 리스트 조회
    public List<OutsourcingMaterialReleaseResponse> getOutsourcingMeterials(Long productionId){
        return outsourcingMaterialRepository.findAllUseYn(productionId);
    }

    //외주생산 원재료 출고 대상 단일 조회
    public OutsourcingMaterialReleaseResponse getOutsourcingMaterial(Long requestId, Long materialId) throws NotFoundException {
        OutSourcingProductionRequest request = outsourcingProductionRepository.findByIdAndDeleteYnFalse(requestId).orElseThrow(()-> new NotFoundException("outsourcinginput not in db:" + requestId));
        return outsourcingMaterialRepository.findByMaterialId(materialId).orElseThrow(()-> new NotFoundException("outsourcinginput not in db:" + materialId));
    }

    //외주생산 원재료 출고 대상 수정
    public OutsourcingMaterialReleaseResponse modifyOutsourcingMaterial(Long requestId, Long materialId, OutsourcingMaterialReleaseRequest request) throws NotFoundException {
        OutSourcingProductionRawMaterialOutputInfo info = outsourcingMaterialRepository.findByIdAndDeleteYnFalse(materialId)
                .orElseThrow(() -> new IllegalArgumentException("not found data"));
        BomItemDetail itemDetail = bomItemDetailRepository.getById(request.getItemDetailId());
        info.update(request, itemDetail);
        outsourcingMaterialRepository.save(info);
        return outsourcingMaterialRepository.findByMaterialId(materialId).orElseThrow(()-> new NotFoundException("outsourcinginput not in db:" + materialId));
    }

    //외주생산 원재료 출고 대상 삭제
    public void deleteOutsourcingMaterial(Long requestId, Long id){
        OutSourcingProductionRequest request = outsourcingProductionRepository.findByIdAndDeleteYnFalse(requestId).orElseThrow(() -> new IllegalArgumentException("not found data"));
        OutSourcingProductionRawMaterialOutputInfo info = outsourcingMaterialRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("not found data"));
        info.delete();
        outsourcingMaterialRepository.save(info);
    }

    //외주 입고정보 등록
    public OutsourcingInputResponse createOutsourcingInput(OutsourcingInputRequest request) throws NotFoundException {
        OutSourcingInput input = modelMapper.toEntity(request, OutSourcingInput.class);
        input.setProductionRequest(outsourcingProductionRepository.findByIdAndDeleteYnFalse(request.getRequestId()).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + request.getRequestId())));
        input.setInputWareHouse(wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId()).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + request.getWarehouseId())));
        outsourcingInputRepository.save(input);
        return outsourcingInputRepository.findInputByIdAndDeleteYnAndUseYn(input.getId()).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + input.getId()));
    }

    //외주 입고정보 리스트조회
    public List<OutsourcingInputResponse> getOutsourcingInputList(Long clientId, String itemNo, String itemName, LocalDate startDate, LocalDate endDate){
        List<OutsourcingInputResponse> allByCondition = outsourcingInputRepository.findAllByCondition(clientId, itemNo, itemName, startDate, endDate);
        for (OutsourcingInputResponse response : allByCondition) {
            List<OutSourcingInput> allByRequestId = outsourcingInputRepository.findAllByRequestId(response.getId());
            if(allByRequestId.size() > 0){
                response.setInputDate(allByRequestId.get(0).getInputDate());
                response.setInputAmount(allByRequestId.stream().mapToInt(OutSourcingInput::getInputAmount).sum());
                if(allByRequestId.get(0).getInputWareHouse() != null){
                    response.setWarehouseName(allByRequestId.get(0).getInputWareHouse().getWareHouseName());
                }
                else{
                    response.setWarehouseName("");
                }
            }
            Integer amount = outsourcingInputRepository.findAmountByRequestId(response.getId());
//            response.setInputTestYn(allByRequestId.get(0).get);
            response.setNoInputAmount(amount - response.getInputAmount());
        }
        //.stream().map(OutsourcingInputResponse::setOutSourcingInputTestItemName).collect(Collectors.toList());

        return allByCondition;
    }

    //외주 입고정보 조회
    public OutsourcingInputResponse getOutsourcingInput(Long inputId) throws NotFoundException {
        return outsourcingInputRepository.findInputByIdAndDeleteYnAndUseYn(inputId).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + inputId));
    }

    //외주 입고정보 수정
    public OutsourcingInputResponse modifyOutsourcingInput(Long inputId, OutsourcingInputRequest request) throws NotFoundException {
        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(inputId).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + inputId));
        OutSourcingProductionRequest prodRequest = outsourcingProductionRepository.findByIdAndDeleteYnFalse(request.getRequestId()).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + inputId));
        WareHouse wareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId()).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + inputId));
        input.update(request, prodRequest, wareHouse);
        outsourcingInputRepository.save(input);
        return outsourcingInputRepository.findInputByIdAndDeleteYnAndUseYn(inputId).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + inputId));
    }

    //외주 입고정보 삭제
    public void deleteOutsourcingInput(Long requestId) throws NotFoundException {
        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(requestId).orElseThrow(()-> new NotFoundException("outsourcingInfo not in db:" + requestId));;
        input.delete();
        outsourcingInputRepository.save(input);
    }

    //외주 입고 LOT정보 등록
    public OutsourcingInputLOTResponse createOutsourcingInputLOT(Long requestId, OutsourcingInputLOTRequest request)
            throws NotFoundException, BadRequestException {

        OutSourcingProductionRequest productionRequest = outsourcingProductionRepository.findByIdAndDeleteYnFalse(requestId)
                .orElseThrow(() -> new NotFoundException("외주생산의뢰가 존재하지 않습니다. id: " + requestId));
        List<OutSourcingInput> allByRequestId = outsourcingInputRepository.findAllByRequestId(requestId);
        OutSourcingInput input = new OutSourcingInput();
        WareHouse wareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("해당 창고가 존재하지 않습니다. id: " + requestId));
        //외주입고 생성
        //이미 외주입고가 존재 할 경우, 기존 정보와 합침
        if(allByRequestId.size() > 0){
            input.setProductionRequest(productionRequest);
            input.setInputDate(LocalDate.now());
            input.setInputAmount(allByRequestId.stream().mapToInt(OutSourcingInput::getInputAmount).sum() + request.getInputAmount());
            input.setInputWareHouse(wareHouse);
            input.setInputTestYn(request.isInputTestYn());
        }
        //외주입고 정보가 존재 하지 않을 경우, 새롭게 생성
        else{
            input.setProductionRequest(productionRequest);
            input.setInputDate(LocalDate.now());
            input.setInputAmount(request.getInputAmount());
            input.setInputTestYn(request.isInputTestYn());
            input.setInputWareHouse(wareHouse);
        }

        outsourcingInputRepository.save(input);

        //Lot 생성
        LotMasterRequest lotMasterRequest = new LotMasterRequest();
        //수입검사여부가 true일 경우 재고 수량 0, 아닐 경우 생성 수량과 동일하게
        if(input.getProductionRequest().isInputTestYn() == true){
            lotMasterRequest.putOutsourcingInputLotRequest(
                    input.getProductionRequest().getBomMaster().getItem(),
                    input.getInputWareHouse(),
                    input,
                    0,
                    request.getInputAmount(),
                    input.getProductionRequest().getBomMaster().getItem().getLotType()
            );
        }
        else{
            lotMasterRequest.putOutsourcingInputLotRequest(
                    input.getProductionRequest().getBomMaster().getItem(),
                    input.getInputWareHouse(),
                    input,
                    request.getInputAmount(),
                    request.getInputAmount(),
                    input.getProductionRequest().getBomMaster().getItem().getLotType()
            );
        }

        //LOT번호 생성
        String lotNo = lotHelper.createLotMaster(lotMasterRequest).getLotNo();

        LotMaster lotMaster = lotMasterRepository.findByLotNoAndDeleteYnFalse(lotNo)
                .orElseThrow(()-> new NotFoundException("해당 LOT번호가 존재하지 않습니다." + lotNo));

        amountHelper.amountUpdate(lotMaster.getItem().getId(), input.getInputWareHouse().getId(), null,   ItemLogType.STORE_AMOUNT, request.getInputAmount(), true);

        OutsourcingInputLOTResponse lotResponse = new OutsourcingInputLOTResponse();

        lotResponse.setLotNo(lotNo);
        lotResponse.setLotId(lotMaster.getId());
        lotResponse.setId(lotMaster.getOutSourcingInput().getId());
        lotResponse.setLotType(lotMaster.getLotType().getLotType());
        lotResponse.setInputAmount(lotMaster.getCreatedAmount());
        lotResponse.setTestRequestType(lotMaster.getOutSourcingInput().getTestRequestType());

        return lotResponse;
    }

    //외주 입고 LOT정보 리스트조회
    public List<OutsourcingInputLOTResponse> getOutsourcingInputLOTList(Long requestId) throws NotFoundException {
        List<OutSourcingInput> inputList = outsourcingInputRepository.findAllByRequestId(requestId);
        List<OutsourcingInputLOTResponse> lotList = new ArrayList<>();
        for (OutSourcingInput outSourcingInput : inputList) {
            LotMaster lotMaster = lotMasterRepository.findByOutsourcingInput(outSourcingInput.getId());
            if(lotMaster == null){
                break;
            }
            OutsourcingInputLOTResponse response = new OutsourcingInputLOTResponse();
            response.setId(outSourcingInput.getId());
            response.setLotId(lotMaster.getId());
            response.setLotType(lotMaster.getLotType().getLotType());
            response.setLotNo(lotMaster.getLotNo());
            response.setInputAmount(lotMaster.getCreatedAmount());
            response.setTestRequestType(outSourcingInput.getTestRequestType());
            response.setInputTestYn(outSourcingInput.isInputTestYn());
            response.setWarehouseId(outSourcingInput.getInputWareHouse().getId());
            response.setWarehouseName(outSourcingInput.getInputWareHouse().getWareHouseName());
            lotList.add(response);
        }
        return lotList;
    }

    //외주 입고 LOT정보 조회
    public OutsourcingInputLOTResponse getOutsourcingInputLOT(Long requestId, Long inputId) throws NotFoundException, BadRequestException {
        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(inputId).orElseThrow(()-> new NotFoundException("해당 입고 정보가 존재하지 않습니다:" + inputId));;
        LotMaster lotMaster = lotMasterRepository.findByOutsourcingInput(inputId);
        if(lotMaster == null){
            throw new BadRequestException("해당 LOT정보가 존재하지 않습니다.");
        }
        OutsourcingInputLOTResponse response = new OutsourcingInputLOTResponse();
        response.setId(input.getId());
        response.setLotId(lotMaster.getId());
        response.setLotType(lotMaster.getLotType().getLotType());
        response.setLotNo(lotMaster.getLotNo());
        response.setInputAmount(lotMaster.getCreatedAmount());
        response.setTestRequestType(input.getTestRequestType());
        response.setInputTestYn(input.isInputTestYn());
        response.setWarehouseId(input.getInputWareHouse().getId());
        response.setWarehouseName(input.getInputWareHouse().getWareHouseName());

        return response;
    }

    //외주 입고 LOT정보 수정
    public Long modifyOutsourcingInputLOT(Long requestId, Long inputId, OutsourcingInputLOTRequest request) throws NotFoundException, BadRequestException {
        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(inputId).orElseThrow(()-> new NotFoundException("해당 입고 정보가 존재하지 않습니다:" + inputId));
        WareHouse wareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId()).orElseThrow(()-> new NotFoundException("해당 창고가 존재하지 않습니다:" + inputId));
        LotMaster lotInfo = lotMasterRepository.findByOutsourcingInput(inputId);
        if(lotInfo == null){
            throw new BadRequestException("해당 입고 LOT정보가 존재하지 않습니다.");
        }
        if(input.isInputTestYn() == true){
            if(lotInfo.getStockAmount() > 0){
                new BadRequestException("수입검사가 진행중이거나 완료된 입고 건은 수정 할 수 없습니다.");
            }
            input.setInputAmount(input.getInputAmount() + (request.getInputAmount() - lotInfo.getCreatedAmount()));
            lotInfo.setCreatedAmount(request.getInputAmount());
            lotInfo.setLotType(input.getProductionRequest().getBomMaster().getItem().getLotType());
            lotInfo.setWareHouse(wareHouse);
            lotMasterRepository.save(lotInfo);
        }
        else{
            if(lotInfo.getCreatedAmount() != lotInfo.getStockAmount()){
                new BadRequestException("재고수량과 입고수량이 다를 경우 수정 할 수 없습니다.");
            }
            input.setInputAmount(input.getInputAmount() + (request.getInputAmount() - lotInfo.getCreatedAmount()));
            lotInfo.setCreatedAmount(request.getInputAmount());
            lotInfo.setStockAmount(request.getInputAmount());
            lotInfo.setLotType(input.getProductionRequest().getBomMaster().getItem().getLotType());
            lotInfo.setWareHouse(wareHouse);
            lotMasterRepository.save(lotInfo);
        }
        return inputId;
    }

    //외주 입고 LOT정보 삭제
    public void deleteOutsourcingInputLOT(Long requestId, Long inputId) throws NotFoundException, BadRequestException {
        LotMaster lotInfo = lotMasterRepository.findByOutsourcingInput(inputId);
        if(lotInfo == null){
            throw new BadRequestException("해당 LOT정보가 존재하지 않습니다.");
        }

        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(inputId).orElseThrow(()-> new NotFoundException("외주입고정보가 존재하지 않습니다."));
        input.setInputAmount(input.getInputAmount() - lotInfo.getCreatedAmount());
        input.setDeleteYn(true);
        outsourcingInputRepository.save(input);
        lotInfo.delete();
        lotMasterRepository.save(lotInfo);
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
        if(returning.isReturnDivision() == true && (lotMaster.getStockAmount() + lotMaster.getStockReturnAmount()) == (request.getStockAmount() + request.getStockReturnAmount())){
            lotMaster.setStockAmount(request.getStockAmount());
            lotMaster.setStockReturnAmount(request.getStockReturnAmount());
        }
        else if(request.isReturnDivision() == false && (lotMaster.getBadItemAmount() + lotMaster.getBadItemReturnAmount()) == (request.getBadItemAmount()) + request.getBadItemReturnAmount()){
            lotMaster.setBadItemAmount(request.getBadItemAmount());
            lotMaster.setBadItemReturnAmount(request.getBadItemReturnAmount());
        }
        else{
            throw new BadRequestException("returnAmount can not bigger than stockAmount or badItemAmount");
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
}
