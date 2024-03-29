package com.mes.mesBackend.helper.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.entity.enumeration.GoodsType;
import com.mes.mesBackend.entity.enumeration.LotMasterDivision;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.LotHelper;
import com.mes.mesBackend.helper.LotLogHelper;
import com.mes.mesBackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.mes.mesBackend.entity.enumeration.EnrollmentType.OUTSOURCING_INPUT;
import static com.mes.mesBackend.entity.enumeration.EnrollmentType.PURCHASE_INPUT;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.*;
import static com.mes.mesBackend.helper.Constants.*;

@Component
@RequiredArgsConstructor
public class LotHelperImpl implements LotHelper {
    private final static String PRODUCT_HEADER = "MFG";
    private final static String DUMMY_HEADER = "POP";
    private final static String EQU_HEADER = "EQU";
    private final LotMasterRepository lotMasterRepo;
    private final EquipmentRepository equipmentRepository;
    private final LotLogHelper lotLogHelper;
    private final WorkProcessRepository workProcessRepository;
    private final static String FORMAT_04 = "%04d";
    private final static String FORMAT_03 = "%03d";

    @Override
    public LotMaster createLotMaster(LotMasterRequest lotMasterRequest) throws NotFoundException, BadRequestException {
        Long workProcessId = lotMasterRequest.getWorkProcessDivision() != null ? lotLogHelper.getWorkProcessByDivisionOrThrow(lotMasterRequest.getWorkProcessDivision()) : null;
        WorkProcess workProcess = workProcessId != null ? getWorkProcessIdOrThrow(workProcessId) : null;
        Equipment fillingInputEquipment = lotMasterRequest.getFillingEquipmentId() != null ? getEquipmentOrThrow(lotMasterRequest.getFillingEquipmentId()) : null;

        // 설비 -> 설비값이 없을경우 공정에 해당하는 첫번째 설비로 등록
        Equipment equipment = lotMasterRequest.getEquipmentId() != null ? getEquipmentOrThrow(lotMasterRequest.getEquipmentId()) : equipmentRepository.findByWorkProcess(workProcess.getId());
        EnrollmentType enrollmentType = lotMasterRequest.getEnrollmentType();

        LotMaster lotMaster = new LotMaster();

        if (enrollmentType.equals(PURCHASE_INPUT)) {
            String lotNo = createLotNo(lotMasterRequest.getItem(), null, lotMasterRequest.getLotMasterDivision());
            lotMaster.createPurchaseInputLot(lotMasterRequest, lotNo, workProcess);
        } else if (enrollmentType.equals(OUTSOURCING_INPUT)) {
            String lotNo = createLotNo(lotMasterRequest.getItem(), null, lotMasterRequest.getLotMasterDivision());
            lotMaster.createOutsourcingInputLot(lotMasterRequest, lotNo, workProcess);
        } else {
            String lotNo = createLotNo(lotMasterRequest.getItem(), equipment, lotMasterRequest.getLotMasterDivision());
            lotMaster.createWorkProcessLot(lotMasterRequest, workProcess, equipment, lotNo, fillingInputEquipment);
        }

        lotMasterRepo.save(lotMaster);
        return lotMaster;
    }

    @Override
    public String createLotNo(Item item, Equipment equipment, LotMasterDivision lotMasterDivision) throws BadRequestException {
        GoodsType goodsType = item.getItemAccount().getGoodsType();
        LocalDate now = LocalDate.now();
        String itemAccountCode = item.getItemAccountCode().getCode();
        String lotNo = null;

        if (lotMasterDivision.equals(DUMMY_LOT) || lotMasterDivision.equals(EQUIPMENT_LOT)) {
            // dummyLot, equipmentLot 중 금일 등록된 lot
            String beforeDummyLotNoOrEquipmentLotNo = lotMasterRepo.findDummyNoByDivision(lotMasterDivision, now).orElse(null);
            lotNo = createLotDivisionLotNo(beforeDummyLotNoOrEquipmentLotNo, lotMasterDivision);
        }
        else if (lotMasterDivision.equals(REAL_LOT)) {
            // 원부자재, 반제품 중 금일 등록된 lot
            String beforeRealLotNo = lotMasterRepo.findLotNoByAccountCodeAndDate(goodsType, now).orElse(null);
            switch (goodsType) {
                case RAW_MATERIAL:
                case SUB_MATERIAL: lotNo = createRawAndSubMaterialLotNo(beforeRealLotNo, itemAccountCode);
                    break;
                case HALF_PRODUCT:
                    // equipment 가 null 일 경우는 외주생산입고일 경우임
                    lotNo = createHalfProductLotNo(beforeRealLotNo, itemAccountCode, equipment != null ? equipment.getLotCode() : "");
                    break;
                case PRODUCT:
                    // 완제품 중 해당하는 달에 등록된 lot, 조건: 품목계정, 설비, 해당하는 달
                    // equipment 가 null 일 경우는 외주생산입고일 경우임
                    String beforeProductRealLotNo = lotMasterRepo.findLotNoByAccountCodeAndMonth(goodsType, now).orElse(null);
                    lotNo = createProductLotNo(beforeProductRealLotNo, itemAccountCode, equipment != null ? equipment.getLotCode() : "");
                    break;
                case NONE: throw new BadRequestException("해당 품목에 대한 품목계정이 존재하지 않습니다.");
            }
        }
        return lotNo;
    }

    // 원부자재 lotNo 생성
    private String createRawAndSubMaterialLotNo(String beforeLotNo, String itemAccountCode) {
        return beforeLotNo != null
                ? getDateFormatYymmdd() + itemAccountCode + String.format(FORMAT_04, lotNoSeq(4, beforeLotNo))
                : getDateFormatYymmdd() + itemAccountCode + String.format(FORMAT_04, 1);
    }

    // 반제품 lotNo 생성
    private String createHalfProductLotNo(String beforeLotNo, String itemAccountCode, String equipmentLotCode) {
        return beforeLotNo != null
                ? getDateFormatYymmdd() + itemAccountCode + equipmentLotCode + String.format(FORMAT_03, lotNoSeq(3, beforeLotNo))
                : getDateFormatYymmdd() + itemAccountCode + equipmentLotCode + String.format(FORMAT_03, 1);
    }

    // 완제품 lotNo 생성
    private String createProductLotNo(String beforeLotNo, String itemAccountCode, String equipmentLotCode) {
        return beforeLotNo != null
                ? PRODUCT_HEADER + getDateFormatYymmdd() + itemAccountCode + equipmentLotCode + String.format(FORMAT_04, lotNoSeq(4, beforeLotNo))
                : PRODUCT_HEADER + getDateFormatYymmdd() + itemAccountCode + equipmentLotCode + String.format(FORMAT_04, 1);
    }

    // dummyLot, equipmentLot 번호 생성
    private String createLotDivisionLotNo(String beforeLotNo, LotMasterDivision division) {
        String headerFormat = division.equals(DUMMY_LOT) ? DUMMY_HEADER : division.equals(EQUIPMENT_LOT) ? EQU_HEADER : null;
        return beforeLotNo != null
                ? headerFormat + getDateFormatYymmdd() + String.format(FORMAT_04, lotNoSeq(4, beforeLotNo))
                : headerFormat + getDateFormatYymmdd() + String.format(FORMAT_04, 1);
    }

    // seq 생성
    private int lotNoSeq(int index, String beforeLotNo) {
        return Integer.parseInt(beforeLotNo.substring(beforeLotNo.length() - index)) + 1;
    }

    private String getDateFormatYymmdd() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(YYMMDD));
    }

//    // lot 생성
//    @Override
//    public LotMaster createLotMaster(LotMasterRequest lotMasterRequest) throws NotFoundException, BadRequestException {
//        LotType lotType = lotMasterRequest.getLotTypeId() != null ? getLotTypeOrThrow(lotMasterRequest.getLotTypeId()) : null;
//        PurchaseInput purchaseInput = lotMasterRequest.getPurchaseInputId() != null ? getPurchaseInputOrThrow(lotMasterRequest.getPurchaseInputId()) : null;
//        OutSourcingInput outSourcingInput = lotMasterRequest.getOutsourcingInputId() != null ? getOutsourcingInputOrThrow(lotMasterRequest.getOutsourcingInputId()) : null;
//        Long workProcessId = lotMasterRequest.getWorkProcessDivision() != null ? lotLogHelper.getWorkProcessByDivisionOrThrow(lotMasterRequest.getWorkProcessDivision()) : null;
//        WorkProcess workProcess = workProcessId != null ? getWorkProcessIdOrThrow(workProcessId) : null;
//        // 설비 -> 설비값이 없을경우 공정에 해당하는 첫번째 설비로 등록
//        Equipment equipment = lotMasterRequest.getEquipmentId() != null ? getEquipmentOrThrow(lotMasterRequest.getEquipmentId()) : equipmentRepository.findByWorkProcess(workProcess.getId());
//
//        LotMaster lotMaster = new LotMaster();
//        lotMaster.setItem(lotMasterRequest.getItem());
//        lotMaster.setWareHouse(lotMasterRequest.getWareHouse());
//        lotMaster.setLotType(lotType);
//        lotMaster.setSerialNo(lotMasterRequest.getSerialNo());
//        lotMaster.setEnrollmentType(lotMasterRequest.getEnrollmentType());
//        lotMaster.setProcessYn(lotMasterRequest.isProcessYn());
//        lotMaster.setStockAmount(lotMasterRequest.getStockAmount());
//        lotMaster.setCreatedAmount(lotMasterRequest.getCreatedAmount());
//        lotMaster.setBadItemAmount(lotMasterRequest.getBadItemAmount());
//        lotMaster.setInputAmount(lotMasterRequest.getInputAmount());
//        lotMaster.setRecycleAmount(lotMasterRequest.getRecycleAmount());
//        lotMaster.setPurchaseInput(purchaseInput);
//        lotMaster.setWorkProcess(workProcess);
//        lotMaster.setLotMasterDivision(lotMasterRequest.getLotMasterDivision());
//        lotMaster.setEquipment(equipment);
//
//        GoodsType goodsType = null;
//
//        // 구매입고
//        if (purchaseInput != null) {
//            Long itemId = purchaseInputRepo.findItemIdByPurchaseInputId(purchaseInput.getId());
//            String lotNo = createLotNo(itemId, equipment.getId(), purchaseInput.getId(), lotMaster.getLotMasterDivision());
//            ItemAccountCode itemAccountCode = lotMasterRepo.findCodeByItemId(itemId);
//            switch (itemAccountCode.getItemAccount().getAccount()){
//                case "원자재":
//                    goodsType = GoodsType.RAW_MATERIAL;
//                    break;
//                case "부자재":
//                    goodsType = GoodsType.SUB_MATERIAL;
//                    break;
//                case "반제품":
//                    goodsType = GoodsType.HALF_PRODUCT;
//                    break;
//                case "완제품":
//                    goodsType = GoodsType.PRODUCT;
//                default:
//                    goodsType = GoodsType.NONE;
//            }
//            lotMaster.createPurchaseInputLot(lotType, purchaseInput, lotNo, workProcess); // 등록유형 PURCHASE_INPUT
//        }
//        else if(outSourcingInput != null) {
//            Long itemId = outsourcingInputRepository.findItemIdByInputId(outSourcingInput.getId());
//            String lotNo = createLotNo(itemId, equipment.getId(),outSourcingInput.getId(), lotMaster.getLotMasterDivision());
//            ItemAccountCode itemAccountCode = lotMasterRepo.findCodeByItemId(itemId);
//            switch (itemAccountCode.getItemAccount().getAccount()){
//                case "원자재":
//                    goodsType = GoodsType.RAW_MATERIAL;
//                    break;
//                case "부자재":
//                    goodsType = GoodsType.SUB_MATERIAL;
//                    break;
//                case "반제품":
//                    goodsType = GoodsType.HALF_PRODUCT;
//                    break;
//                case "완제품":
//                    goodsType = GoodsType.PRODUCT;
//                default:
//                    goodsType = GoodsType.NONE;
//            }
//            lotMaster.createOutsourcingInputLot(lotType, outSourcingInput, lotNo);
//        }
//        else{
//            lotMaster.setLotNo(createLotNo(lotMasterRequest.getItem().getId(), equipment.getId(), null, lotMaster.getLotMasterDivision()));
//        }
//
//        lotMasterRepo.save(lotMaster);
//        return lotMaster;
//    }


//    // lot 번호 생성
//    public String createLotNo(Long itemId, Long equipmentId, Long deleteId, LotMasterDivision lotMasterDivision) throws BadRequestException {
//        String createdLotNo = null;
//        // 1~6 입고년월일 예) 21년 12월 11일 211211
//        String dateCode = LocalDate.now().format(DateTimeFormatter.ofPattern(YYMMDD));
//        String productDateCode = LocalDate.now().format(DateTimeFormatter.ofPattern(YYMM));
//        Equipment equipment = equipmentRepository.findByIdAndDeleteYnFalse(equipmentId)
//                .orElse(null);
//
//        String equipmentNo = "";
//
//        if(equipment != null){
//             equipmentNo = equipment.getEquipmentCode();
//        }
//
//        // 품목의 품목계정 코드 조회
//        ItemAccountCode itemAccountCode = lotMasterRepo.findCodeByItemId(itemId);
//        String code = itemAccountCode.getCode();
//
//        LocalDate startDate = LocalDate.now();
//        LocalDate endDate = LocalDate.now().plusDays(1);
//
//        String beforeRealLotNo = lotMasterRepo.findLotNoByAccountCodeAndDate(itemAccountCode.getId(), LocalDate.now())
//                .orElse(null);
//        // 생성날짜가 오늘이고, lotDivision 이 dummny 인 걸 찾아옴
//        String beforeDummyOrEquipmentLotNo = lotMasterRepo.findDummyNoByDivision(lotMasterDivision, startDate).orElse(null);
//
//        startDate = LocalDate.now().withDayOfMonth(1);
//        endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
//        String productLotNo = lotMasterRepo.findLotNoByAccountCodeAndDate(itemAccountCode.getId(), startDate)
//                .orElse(null);
//        int seq = 1;
//
//        switch (itemAccountCode.getItemAccount().getGoodsType()){
//            case RAW_MATERIAL:
//            case SUB_MATERIAL:
//                if(beforeRealLotNo == null){
//                    createdLotNo = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + code
//                            + String.format("%04d", seq);
//                }
//                else{
//                    seq = Integer.parseInt(Objects.requireNonNull(beforeRealLotNo).substring(beforeRealLotNo.length() - 4)) + 1;
//                    createdLotNo = dateCode + code + String.format("%04d", seq);
//                }
//                break;
//            case HALF_PRODUCT:
//                // dummyLot
//                if (lotMasterDivision.equals(DUMMY_LOT)) {
//                    if (beforeDummyOrEquipmentLotNo == null) {
//                        createdLotNo = DUMMY_HEADER + NOW_YYMMDD + String.format("%04d", seq);
//                    } else {
//                        seq = Integer.parseInt(beforeDummyOrEquipmentLotNo.substring(beforeDummyOrEquipmentLotNo.length() - 4)) + 1;
//                        createdLotNo = DUMMY_HEADER + NOW_YYMMDD + String.format("%04d", seq);
//                    }
//                }
//                // equipmentLot
//                if (lotMasterDivision.equals(EQUIPMENT_LOT)) {
//                    if (beforeDummyOrEquipmentLotNo == null) {
//                        createdLotNo = EQU_HEADER + NOW_YYMMDD + String.format("%04d", seq);;
//                    } else {
//                        seq = Integer.parseInt(beforeDummyOrEquipmentLotNo.substring(beforeDummyOrEquipmentLotNo.length() -4)) + 1;
//                        createdLotNo = EQU_HEADER + NOW_YYMMDD + String.format("%04d", seq);
//                    }
//
//                }
//                // 분할 lot
//                if (lotMasterDivision.equals(REAL_LOT)) {
//                    if(beforeRealLotNo == null){
//                        createdLotNo = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + code
//                                + equipmentNo + String.format("%03d", seq);
//                    }
//                    else{
//                        seq = Integer.parseInt(Objects.requireNonNull(beforeRealLotNo).substring(beforeRealLotNo.length() - 3)) + 1;
//                        createdLotNo = dateCode + code + equipmentNo + String.format("%03d", seq);
//                    }
//                }
//                break;
//            case PRODUCT:
//                // dummyLot
//                if (lotMasterDivision.equals(DUMMY_LOT)) {
//                    if (beforeDummyOrEquipmentLotNo == null) {
//                        createdLotNo = DUMMY_HEADER + NOW_YYMMDD + String.format("%04d", seq);
//                    } else {
//                        seq = Integer.parseInt(beforeDummyOrEquipmentLotNo.substring(beforeDummyOrEquipmentLotNo.length() - 4)) + 1;
//                        createdLotNo = DUMMY_HEADER + NOW_YYMMDD + String.format("%04d", seq);
//                    }
//                }
//                // equipmentLot
//                if (lotMasterDivision.equals(EQUIPMENT_LOT)) {
//                    if (beforeDummyOrEquipmentLotNo == null) {
//                        createdLotNo = EQU_HEADER + NOW_YYMMDD + String.format("%04d", seq);;
//                    } else {
//                        seq = Integer.parseInt(beforeDummyOrEquipmentLotNo.substring(beforeDummyOrEquipmentLotNo.length() -4)) + 1;
//                        createdLotNo = EQU_HEADER + NOW_YYMMDD + String.format("%04d", seq);
//                    }
//
//                }
//                // 분할 lot
//                if (lotMasterDivision.equals(REAL_LOT)) {
//                    if(productLotNo == null){
//                        createdLotNo = PRODUCT_HEADER + productDateCode + code + equipmentNo
//                                + String.format("%04d", seq);
//                    }
//                    else{
//                        seq = Integer.parseInt(Objects.requireNonNull(beforeRealLotNo).substring(beforeRealLotNo.length() - 4)) + 1;
//                        createdLotNo = PRODUCT_HEADER + productDateCode + code + equipmentNo + String.format("%04d", seq);
//                    }
//                }
//                break;
//            case NONE:
//                createdLotNo = null;
//        }
//        return createdLotNo;
//    }

    private WorkProcess getWorkProcessIdOrThrow(Long id) throws NotFoundException {
        return workProcessRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("workProcess does not exist. input id: " + id));
    }

    // 설비 단일 조회 및 예외
    private Equipment getEquipmentOrThrow(Long equipmentId) throws NotFoundException {
        return equipmentRepository.findByIdAndDeleteYnFalse(equipmentId)
                .orElseThrow(() -> new NotFoundException("[LotHelper] equipment does not exist. "));
    }
}
