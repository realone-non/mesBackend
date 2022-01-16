package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.*;
import com.mes.mesBackend.dto.response.*;

import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.ItemLogType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.AmountHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.OutsourcingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OutsourcingServiceImpl implements OutsourcingService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    OutSourcingProductionRequestRepository outsourcingProductionRepository;
    @Autowired
    OutSourcingProductionRawMaterialOutputInfoRepository outsourcingMaterialRepository;
    @Autowired
    OutsourcingInputRepository outsourcingInputRepository;
    @Autowired
    OutsourcingReturnRepository outsourcingReturnRepository;
    @Autowired
    BomMasterRepository bomMasterRepository;
    @Autowired
    BomItemDetailRepository bomItemDetailRepository;
    @Autowired
    LotMasterService lotMasterService;
    @Autowired
    LotMasterRepository lotMasterRepository;
    @Autowired
    WareHouseRepository wareHouseRepository;
    @Autowired
    AmountHelper amountHelper;


    //외주생산의뢰 등록
    @Override
    public OutsourcingProductionResponse createOutsourcingProduction(OutsourcingProductionRequestRequest outsourcingProductionRequestRequest)  {
        OutSourcingProductionRequest request = modelMapper.toEntity(outsourcingProductionRequestRequest, OutSourcingProductionRequest.class);
        request.setBomMaster(bomMasterRepository.getById(outsourcingProductionRequestRequest.getBomNo()));
        request.setProductionDate(LocalDate.now());
        outsourcingProductionRepository.save(request);
        return modelMapper.toResponse(request, OutsourcingProductionResponse.class);
    }

    //외주생산의뢰 리스트조회
    public List<OutsourcingProductionResponse> getOutsourcingProductions(Long clientId, Long itemNo, LocalDate startDate, LocalDate endDate){
        return outsourcingProductionRepository.findAllByCondition(clientId, itemNo, startDate, endDate);
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
        BomMaster bomMaster = bomMasterRepository.getById((outsourcingProduction.getBomNo()));
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
    public List<OutsourcingInputResponse> getOutsourcingInputList(Long clientId, Long itemId, LocalDate startDate, LocalDate endDate){
        return outsourcingInputRepository.findAllByCondition(clientId, itemId, startDate, endDate);
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
    public void deleteOutsourcingInput(Long id) throws NotFoundException {
        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(id).orElseThrow(()-> new NotFoundException("outsourcingInfo not in db:" + id));;
        input.delete();
        outsourcingInputRepository.save(input);
    }

    //외주 입고 LOT정보 등록
    public OutsourcingInputLOTResponse createOutsourcingInputLOT(Long id, OutsourcingInputLOTRequest request)
            throws NotFoundException, BadRequestException {
        Optional<OutSourcingInput> input = outsourcingInputRepository.findById(id);

        //Lot 생성
        LotMasterRequest lotMasterRequest = new LotMasterRequest();
        lotMasterRequest.putOutsourcingInput(
                input.get().getProductionRequest().getBomMaster().getItem(),
                input.get().getInputWareHouse(),
                input.get().getId(),
                request.getInputAmount(),
                request.getLotType()
        );

        String lotNo = lotMasterService.createLotMaster(lotMasterRequest);

        LotMaster lotMaster = lotMasterRepository.findByLotNoAndUseYnTrue(lotNo);

        amountHelper.amountUpdate(lotMaster.getItem().getId(), input.get().getInputWareHouse().getId(), null,  request.getInputAmount(), ItemLogType.INPUT_AMOUNT, true);

        OutsourcingInputLOTResponse lotResponse = new OutsourcingInputLOTResponse();

        lotResponse.setLotNo(lotNo);
        lotResponse.setLotId(lotMaster.getId());
        lotResponse.setId(lotMaster.getOutSourcingInput().getId());
        lotResponse.setLotType(lotMaster.getLotType().getLotType());
        lotResponse.setInputAmount(lotMaster.getStockAmount());
        lotResponse.setTestRequestType(lotMaster.getOutSourcingInput().getTestRequestType());

        return lotResponse;
    }

    //외주 입고 LOT정보 리스트조회
    public List<OutsourcingInputLOTResponse> getOutsourcingInputLOTList(Long inputId){
        return lotMasterRepository.findLotMastersByOutsourcing(inputId);
    }

    //외주 입고 LOT정보 조회
    public OutsourcingInputLOTResponse getOutsourcingInputLOT(Long inputId, Long id) throws NotFoundException {
        OutSourcingInput input = outsourcingInputRepository.findByIdAndDeleteYnFalse(inputId).orElseThrow(()-> new NotFoundException("outsourcinginput not in db:" + inputId));;
        return lotMasterRepository.findLotMasterByInputAndId(input, id);
    }

    //외주 입고 LOT정보 수정
    public OutsourcingInputLOTResponse modifyOutsourcingInputLOT(Long inputId, Long id, OutsourcingInputLOTRequest request) throws NotFoundException {
        LotMaster lotInfo = lotMasterRepository.findByIdAndDeleteYnFalse(id).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + id));
        lotInfo.setInputAmount(request.getInputAmount());
        lotMasterRepository.save(lotInfo);
        return modelMapper.toResponse(lotInfo, OutsourcingInputLOTResponse.class);
    }

    //외주 입고 LOT정보 삭제
    public void deleteOutsourcingInputLOT(Long inputId, Long id) throws NotFoundException {
        LotMaster lotInfo = lotMasterRepository.findByIdAndDeleteYnFalse(id).orElseThrow(()-> new NotFoundException("lotinfo not in db:" + id));
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
    public List<OutsourcingReturnResponse> getOutsourcingReturnList(Long clientId, Long itemId, LocalDate startDate, LocalDate endDate){
        return outsourcingReturnRepository.findAllByCondition(clientId, itemId, startDate, endDate);
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
    public List<OutsourcingStatusResponse> getOutsourcingStatusList(Long clientId, Long itemId){
        return outsourcingInputRepository.findStatusByCondition(clientId, itemId);
    }
}
