package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.response.LotTrackingResponse;
import com.mes.mesBackend.entity.LotConnect;
import com.mes.mesBackend.entity.LotEquipmentConnect;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.repository.LotConnectRepository;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.service.LotTrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    //            List<LotEquipmentConnect> findLotEquipmentConnects = lotMasterRepo.findExhaustLotByLotNoAndTrackTypeTrue(lotNo);
//            for (LotEquipmentConnect findLotEquipmentConnect : findLotEquipmentConnects) {
//                Long equipmentLotId = findLotEquipmentConnect.getChildLot().getId();
//                response.addAll(lotMasterRepo.findLotTrackingResponseByTrackingTypeTrue(equipmentLotId, itemNoAndItemName));
//            }
//            return response;
    @Override
    public List<LotTrackingResponse> getTrackings(String lotNo, boolean trackingType, String itemNoAndItemName) throws NotFoundException {
        List<LotTrackingResponse> responses = new ArrayList<>();
        LotMaster mainLotMaster = getLotMasterOrThrow(lotNo);
        if (trackingType) {     // 정방향?: 이거로 만들어진 애들 조회
            // 만들어진 EquipmentLot 조회
            List<LotConnect> lotConnects = lotConnectRepository.findByChildLotIdAndDivisionIsExhaust(mainLotMaster.getId());    // lotConnectId: 65, 69
            for (LotConnect lotConnect : lotConnects) {
                responses = responses
                        .stream().map(m -> m.setMainLotMaster(lotConnect.getChildLot()))
                        .collect(Collectors.toList())
                        .stream().map(m -> m.setSubLots(lotConnectRepository.findByParentLotAndDivisionIsFamily(lotConnect.getParentLot().getId())))
                        .collect(Collectors.toList());
            }
        } else {                // 역방향
            // lotNo 가 lotConnect 의 childLot 의 lotNo 와 같은걸 찾음.
            LotMaster findEquipmentLot = lotMasterRepo.findEquipmentLotMasterByRealLotNo(lotNo).orElse(null);
            if (findEquipmentLot != null) {
                responses.addAll(lotMasterRepo.findLotTrackingResponseByTrackingTypeFalse(findEquipmentLot.getId(), itemNoAndItemName));
            }
        }
            return responses;
    }


    private LotMaster getLotMasterOrThrow(String lotNo) throws NotFoundException {
        return lotMasterRepo.findByLotNoAndDeleteYnFalse(lotNo)
                .orElseThrow(() -> new NotFoundException("lotMaster does not exist. input lotNo: " + lotNo));
    }
}
