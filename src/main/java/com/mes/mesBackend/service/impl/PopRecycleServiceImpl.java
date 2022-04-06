package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.dto.request.PopRecycleRequest;
import com.mes.mesBackend.dto.response.PopRecycleCreateResponse;
import com.mes.mesBackend.dto.response.PopRecycleResponse;
import com.mes.mesBackend.dto.response.RecycleLotResponse;
import com.mes.mesBackend.dto.response.RecycleResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.LotHelper;
import com.mes.mesBackend.helper.LotLogHelper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.PopRecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.entity.enumeration.EnrollmentType.RECYCLE;
import static com.mes.mesBackend.entity.enumeration.LotMasterDivision.*;

@Service
public class PopRecycleServiceImpl implements PopRecycleService {

    @Autowired
    LotMasterRepository lotMasterRepository;
    @Autowired
    RecycleRepository recycleRepository;
    @Autowired
    LotHelper lotHelper;
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    WareHouseRepository wareHouseRepository;
    @Autowired
    WorkProcessRepository workProcessRepository;
    @Autowired
    LotLogHelper lotLogHelper;
    @Autowired
    LotEquipmentConnectRepository lotEquipmentConnectRepository;

    //재사용 목록 조회
    public List<PopRecycleResponse> getRecycles(WorkProcessDivision workProcessDivision) throws NotFoundException {
        Long workProcessId = lotLogHelper.getWorkProcessByDivisionOrThrow(workProcessDivision);
        return lotMasterRepository.findBadAmountByWorkProcess(workProcessId);
    }

    //재사용 등록
    public PopRecycleCreateResponse createUseRecycle(PopRecycleRequest request) throws NotFoundException, BadRequestException {
        RecycleResponse recycle = recycleRepository.findByIdAndDeleteYn(request.getRecycleId())
                .orElseThrow(() -> new NotFoundException("재사용 유형이 존재하지 않습니다. 입력 id: " + request.getRecycleId()));
        Item item = itemRepository.findByIdAndDeleteYnFalse(request.getItemId())
                .orElseThrow(() -> new NotFoundException("해당 품목이 존재하지 않습니다. 입력 id: " + request.getItemId()));
        WorkProcess workProcess = workProcessRepository.findByIdAndDeleteYnFalse(recycle.getWorkProcessId())
                .orElseThrow(() -> new NotFoundException("해당 공정이 존재하지 않습니다. 입력 id: " + recycle.getWorkProcessId()));
        WareHouse wareHouse = wareHouseRepository.findByWorkProcessYnIsTrueAndDeleteYnFalse()
                .orElseThrow(() -> new NotFoundException("공정용 창고가 존재하지 않습니다. "));
        List<LotMaster> usableEquipmentLotList = lotMasterRepository.findBadLotByItemIdAndWorkProcess(request.getItemId(), recycle.getWorkProcessId(), EQUIPMENT_LOT);

        int createAmount = request.getAmount();
        LotMasterRequest lotRequest = new LotMasterRequest();
        lotRequest.setCreatedAmount(createAmount);
        lotRequest.setStockAmount(createAmount);
        lotRequest.setEnrollmentType(RECYCLE);
        lotRequest.setWareHouse(wareHouse);
        lotRequest.setLotMasterDivision(REAL_LOT);
        lotRequest.setItem(item);
        lotRequest.setWorkProcessDivision(workProcess.getWorkProcessDivision());

        LotMaster lotmaster = lotHelper.createLotMaster(lotRequest);

        for (LotMaster dbLotMaster:usableEquipmentLotList) {
            LotEquipmentConnect dummyLotEquipment = lotEquipmentConnectRepository.findByChildId(dbLotMaster.getId())
                    .orElseThrow(() -> new NotFoundException("DummyLot가 존재하지 않습니다. "));
            LotMaster dummyLot = lotMasterRepository.findByIdAndDeleteYnFalse(dummyLotEquipment.getParentLot().getId())
                    .orElseThrow(() -> new NotFoundException("해당 Lot가 존재하지 않습니다. "));
            if(createAmount <= dbLotMaster.getBadItemAmount()){
                    dbLotMaster.setBadItemAmount(dbLotMaster.getBadItemAmount() - createAmount);
                    dbLotMaster.setRecycleAmount(dbLotMaster.getRecycleAmount() + createAmount);
                    dummyLot.setBadItemAmount(dummyLot.getBadItemAmount() - createAmount);
                    dummyLot.setRecycleAmount(dummyLot.getRecycleAmount() + createAmount);

                    createAmount = 0;
            }
            else if(createAmount > dbLotMaster.getBadItemAmount()){
                int tempAmount = createAmount - dbLotMaster.getBadItemAmount();
                dbLotMaster.setRecycleAmount(dbLotMaster.getBadItemAmount());
                dbLotMaster.setBadItemAmount(0);
                if(createAmount > dummyLot.getBadItemAmount()){
                    dummyLot.setRecycleAmount(dummyLot.getBadItemAmount());
                    dummyLot.setBadItemAmount(0);
                }
                else{
                    dummyLot.setRecycleAmount(dummyLot.getRecycleAmount() + createAmount);
                    dummyLot.setBadItemAmount(dummyLot.getBadItemAmount() - createAmount);
                }
                createAmount = createAmount - dbLotMaster.getBadItemAmount();
            }
            lotMasterRepository.save(dbLotMaster);
            lotMasterRepository.save(dummyLot);

            if(createAmount == 0){
                break;
            }
        }
        PopRecycleResponse badAmountByWorkProcess = lotMasterRepository.findBadAmountByWorkProcess(workProcess.getId(), item.getId());
        PopRecycleCreateResponse response = new PopRecycleCreateResponse();
        response.setLotNo(lotmaster.getLotNo());
        response.setRecycleAmount(badAmountByWorkProcess.getRecycleAmount());

        return response;
    }

    //재사용 LOT조회
    public List<RecycleLotResponse> getRecycleLots(LocalDate fromDate, LocalDate toDate){
        return lotMasterRepository.findRecycleLots(fromDate, toDate);
    }
}
