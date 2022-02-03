package com.mes.mesBackend.helper.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.LotHelper;
import com.mes.mesBackend.helper.LotLogHelper;
import com.mes.mesBackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.mes.mesBackend.helper.Constants.*;

@Component
@RequiredArgsConstructor
public class LotHelperImpl implements LotHelper {

    private String productHeader = "MFG";
    @Autowired
    LotMasterRepository lotMasterRepo;

    @Autowired
    PurchaseInputRepository purchaseInputRepo;

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    OutsourcingInputRepository outsourcingInputRepository;

    @Autowired
    WareHouseRepository wareHouseRepository;

    @Autowired
    LotLogHelper lotLogHelper;

    @Autowired
    WorkProcessRepository workProcessRepository;

    @Autowired
    LotTypeRepository lotTypeRepository;

    // lot 생성
    @Override
    public LotMaster createLotMaster(LotMasterRequest lotMasterRequest) throws NotFoundException, BadRequestException {
        LotType lotType = lotMasterRequest.getLotTypeId() != null ? getLotTypeOrThrow(lotMasterRequest.getLotTypeId()) : null;
        PurchaseInput purchaseInput = lotMasterRequest.getPurchaseInputId() != null ? getPurchaseInputOrThrow(lotMasterRequest.getPurchaseInputId()) : null;
        OutSourcingInput outSourcingInput = lotMasterRequest.getOutsourcingInputId() != null ? getOutsourcingInputOrThrow(lotMasterRequest.getOutsourcingInputId()) : null;
        Long workProcessId = lotMasterRequest.getWorkProcessDivision() != null ? lotLogHelper.getWorkProcessByDivisionOrThrow(lotMasterRequest.getWorkProcessDivision()) : null;
        WorkProcess workProcess = workProcessId != null ? getWorkProcessIdOrThrow(workProcessId) : null;
        Equipment equipment = equipmentRepository.findByWorkProcess(workProcess.getId());
//        LotMaster lotMaster = modelMapper.toEntity(lotMasterRequest, LotMaster.class);

        LotMaster lotMaster = new LotMaster();
        lotMaster.setItem(lotMasterRequest.getItem());
        lotMaster.setWareHouse(lotMasterRequest.getWareHouse());
        lotMaster.setLotType(lotType);
        lotMaster.setSerialNo(lotMasterRequest.getSerialNo());
        lotMaster.setEnrollmentType(lotMasterRequest.getEnrollmentType());
        lotMaster.setProcessYn(lotMasterRequest.isProcessYn());
        lotMaster.setStockAmount(lotMasterRequest.getStockAmount());
        lotMaster.setCreatedAmount(lotMasterRequest.getCreatedAmount());
        lotMaster.setBadItemAmount(lotMasterRequest.getBadItemAmount());
        lotMaster.setInputAmount(lotMasterRequest.getInputAmount());
        lotMaster.setRecycleAmount(lotMasterRequest.getRecycleAmount());
        lotMaster.setPurchaseInput(purchaseInput);
        lotMaster.setWorkProcess(workProcess);
        lotMaster.setDummyYn(lotMasterRequest.isDummyYn());


        GoodsType goodsType = null;

        // 구매입고
        if (purchaseInput != null) {
            Long itemId = purchaseInputRepo.findItemIdByPurchaseInputId(purchaseInput.getId());
            String lotNo = createLotNo(itemId, equipment.getId(), purchaseInput.getId());
            ItemAccountCode itemAccountCode = lotMasterRepo.findCodeByItemId(itemId);
            switch (itemAccountCode.getItemAccount().getAccount()){
                case "원자재":
                    goodsType = GoodsType.RAW_MATERIAL;
                    break;
                case "부자재":
                    goodsType = GoodsType.SUB_MATERIAL;
                    break;
                case "반제품":
                    goodsType = GoodsType.HALF_PRODUCT;
                    break;
                case "완제품":
                    goodsType = GoodsType.PRODUCT;
                default:
                    goodsType = GoodsType.NONE;
            }
            lotMaster.putPurchaseInput(lotType, purchaseInput, lotNo, workProcess); // 등록유형 PURCHASE_INPUT
        }
        else if(outSourcingInput != null) {
            Long itemId = outsourcingInputRepository.findItemIdByInputId(outSourcingInput.getId());
            String lotNo = createLotNo(itemId, equipment.getId(),outSourcingInput.getId());
            ItemAccountCode itemAccountCode = lotMasterRepo.findCodeByItemId(itemId);
            switch (itemAccountCode.getItemAccount().getAccount()){
                case "원자재":
                    goodsType = GoodsType.RAW_MATERIAL;
                    break;
                case "부자재":
                    goodsType = GoodsType.SUB_MATERIAL;
                    break;
                case "반제품":
                    goodsType = GoodsType.HALF_PRODUCT;
                    break;
                case "완제품":
                    goodsType = GoodsType.PRODUCT;
                default:
                    goodsType = GoodsType.NONE;
            }
            lotMaster.putOutsourcingInput(lotType, outSourcingInput, lotNo);
        }
        else{
            lotMaster.setLotNo(createLotNo(lotMasterRequest.getItem().getId(), equipment.getId(), null));
        }

        lotMasterRepo.save(lotMaster);
        return lotMaster;
    }

    // lot 번호 생성
    public String createLotNo(Long itemId, Long equipmentId, Long deleteId) throws BadRequestException {
        String createdLotNo = null;
        // 1~6 입고년월일 예) 21년 12월 11일 211211
        String dateCode = LocalDate.now().format(DateTimeFormatter.ofPattern(YYMMDD));
        String productDateCode = LocalDate.now().format(DateTimeFormatter.ofPattern(YYMM));
        Equipment equipment = equipmentRepository.findByIdAndDeleteYnFalse(equipmentId)
                .orElse(null);

        String equipmentNo = "";

        if(equipment != null){
             equipmentNo = equipment.getEquipmentCode();
        }

        // 품목의 품목계정 코드 조회
        ItemAccountCode itemAccountCode = lotMasterRepo.findCodeByItemId(itemId);
        String code = itemAccountCode.getCode();

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);

        String beforeLotNo = lotMasterRepo.findLotNoByAccountCode(itemAccountCode.getId(), startDate, endDate)
                .orElse(null);
        startDate = LocalDate.now().withDayOfMonth(1);
        endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        String productLotNo = lotMasterRepo.findLotNoByAccountCode(itemAccountCode.getId(), startDate, endDate)
                .orElse(null);
        int seq = 1;

        switch (itemAccountCode.getItemAccount().getGoodsType()){
            case RAW_MATERIAL:
            case SUB_MATERIAL:
                if(beforeLotNo == null){
                    createdLotNo = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + code
                            + String.format("%04d", seq);
                }
                else{
                    seq = Integer.parseInt(Objects.requireNonNull(beforeLotNo).substring(beforeLotNo.length() - 4)) + 1;
                    createdLotNo = dateCode + code + String.format("%04d", seq);
                }
                break;
            case HALF_PRODUCT:
                if(beforeLotNo == null){
                    createdLotNo = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + code
                            + equipmentNo + String.format("%03d", seq);
                }
                else{
                    seq = Integer.parseInt(Objects.requireNonNull(beforeLotNo).substring(beforeLotNo.length() - 4)) + 1;
                    createdLotNo = dateCode + code + equipmentNo + String.format("%03d", seq);
                }
                break;
            case PRODUCT:
                if(productLotNo == null){
                    createdLotNo = productHeader + productDateCode + code + equipmentNo
                            + String.format("%04d", seq);
                }
                else{
                    seq = Integer.parseInt(Objects.requireNonNull(beforeLotNo).substring(beforeLotNo.length() - 4)) + 1;
                    createdLotNo = productHeader + productDateCode + code + equipmentNo + String.format("%04d", seq);
                }
                break;
            case NONE:
                createdLotNo = null;
        }
        return createdLotNo;
    }

    // lotMaster 용 wareHouse 찾기
    private WareHouse getLotMasterWareHouseOrThrow() throws NotFoundException {
        return wareHouseRepository.findByWorkProcessYnIsTrueAndDeleteYnFalse()
                .orElseThrow(() -> new NotFoundException("공정 용 창고가 없습니다. 공정 용 창고 생성 후 다시 시도해 주세요."));
    }

    private WorkProcess getWorkProcessIdOrThrow(Long id) throws NotFoundException {
        return workProcessRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("workProcess does not exist. input id: " + id));
    }

    // 구매입고 단일 조회 및 예외
    private PurchaseInput getPurchaseInputOrThrow(Long id) throws NotFoundException {
        return purchaseInputRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("purchaseInput does not exist. input purchaseInput id: " + id));
    }

    // 외주입고 단일 조회 및 예외
    private OutSourcingInput getOutsourcingInputOrThrow(Long id) throws NotFoundException {
        return outsourcingInputRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("outsourcingInput does not exist. input outsourcingInput id: " + id));
    }

    // Lot유형 조회 및 예외
    private LotType getLotTypeOrThrow(Long id) throws NotFoundException {
        return lotTypeRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("lotType does not exist. input id: " + id));
    }
}
