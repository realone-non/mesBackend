package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.LotTrackingResponse;
import com.mes.mesBackend.entity.LotConnect;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.repository.LotConnectRepository;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.service.LotTrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// 7-2. LOT Tracking
@Service
@RequiredArgsConstructor
public class LotTrackServiceImpl implements LotTrackService {
    private final LotMasterRepository lotMasterRepo;
    private final LotConnectRepository lotConnectRepository;

    // 검색조건: LOT 번호(필수값), 추적유형(필수값), 품명|품번
    /*
    * 추적유형
    *   정방향 true: 입력된 LOT 로 생산된 LOT 조회
    *   역방향 false: 입력된 LOT 를 생산하기 위해 투입된 LOT
    * */

    @Override
    public List<LotTrackingResponse> getTrackings(String lotNo, boolean trackingType, String itemNoAndItemName) throws BadRequestException {
        List<LotTrackingResponse> responses = new ArrayList<>();
        LotMaster mainLotMaster = getLotMasterOrThrow(lotNo);

        if (trackingType) {     // 정방향
            // 만들어진 EquipmentLot 조회
            List<LotConnect> lotConnects = lotConnectRepository.findByChildLotIdAndDivisionIsExhaust(mainLotMaster.getId());
            for (LotConnect lotConnect : lotConnects) {
                LotTrackingResponse lotTrackingResponse = new LotTrackingResponse();
                lotTrackingResponse.setMainLotMaster(lotConnect.getChildLot(), lotConnect.getAmount()); // 검색한 LOT
                lotTrackingResponse.setSubLotMaster(lotConnectRepository.findByParentLotAndDivisionIsFamily(lotConnect.getParentLot().getId())); // 만들어지고 분할 된 LOT
                responses.add(lotTrackingResponse);
            }
            return responses;
        } else {                // 역방향
            // lotNo 가 lotConnect 의 childLot 의 lotNo 와 같은걸 찾음.
            LotTrackingResponse lotTrackingResponse = new LotTrackingResponse();
            lotTrackingResponse.setMainLotMaster(mainLotMaster,0);
            LotMaster findEquipmentLot = lotMasterRepo.findEquipmentLotMasterByRealLotNo(lotNo).orElse(null);
            if (findEquipmentLot != null) {
                lotTrackingResponse.setSubLotMaster(lotMasterRepo.findLotTrackingResponseByTrackingTypeFalse(findEquipmentLot.getId(), itemNoAndItemName));
            }
            responses.add(lotTrackingResponse);
        }
            return responses;
    }


    //            List<LotEquipmentConnect> findLotEquipmentConnects = lotMasterRepo.findExhaustLotByLotNoAndTrackTypeTrue(lotNo);
//            for (LotEquipmentConnect findLotEquipmentConnect : findLotEquipmentConnects) {
//                Long equipmentLotId = findLotEquipmentConnect.getChildLot().getId();
//                response.addAll(lotMasterRepo.findLotTrackingResponseByTrackingTypeTrue(equipmentLotId, itemNoAndItemName));
//            }
//            return response;

//    else {                // 역방향
//        // lotNo 가 lotConnect 의 childLot 의 lotNo 와 같은걸 찾음.
//        LotTrackingResponse lotTrackingResponse = new LotTrackingResponse();
//        lotTrackingResponse.setMainLotMaster(mainLotMaster, mainLotMaster.getCreatedAmount());
//        LotMaster findEquipmentLot = lotMasterRepo.findEquipmentLotMasterByRealLotNo(lotNo).orElse(null);
//        if (findEquipmentLot != null) {
//            lotTrackingResponse.setSubLotMaster(lotMasterRepo.findLotTrackingResponseByTrackingTypeFalse(findEquipmentLot.getId(), itemNoAndItemName));
//        }
////            if (findEquipmentLot != null) {
////                responses.addAll(lotMasterRepo.findLotTrackingResponseByTrackingTypeFalse(findEquipmentLot.getId(), itemNoAndItemName));
////            }
//        responses.add(lotTrackingResponse);
//    }
//            return responses;


    private LotMaster getLotMasterOrThrow(String lotNo) throws BadRequestException {
        return lotMasterRepo.findByLotNoAndDeleteYnFalse(lotNo)
                .orElseThrow(() -> new BadRequestException("입력한 LOT NO 는 존재하지 않습니다. 확인 후 다시 시도해주세요."));
    }
}
