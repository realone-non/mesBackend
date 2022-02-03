package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.dto.request.PopRecycleRequest;
import com.mes.mesBackend.dto.response.LotMasterResponse;
import com.mes.mesBackend.dto.response.PopRecycleResponse;
import com.mes.mesBackend.dto.response.RecycleResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.LotHelper;
import com.mes.mesBackend.helper.impl.LotHelperImpl;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.PopRecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mes.mesBackend.entity.enumeration.EnrollmentType.RECYCLE;

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
    WorkProcessRepository workProcessRepository;

    //재사용 목록 조회
    public List<PopRecycleResponse> getRecycles(Long workProcessId){
        return lotMasterRepository.findBadAmountByWorkProcess(workProcessId);
    }

    //재사용 등록
    public PopRecycleResponse createUseRecycle(PopRecycleRequest request) throws NotFoundException, BadRequestException {
        RecycleResponse recycle = recycleRepository.findByIdAndDeleteYn(request.getRecycleId())
                .orElseThrow(() -> new NotFoundException("재사용 유형이 존재하지 않습니다. 입력 id: " + request.getRecycleId()));
        Item item = itemRepository.findByIdAndDeleteYnFalse(request.getItemId())
                .orElseThrow(() -> new NotFoundException("해당 품목이 존재하지 않습니다. 입력 id: " + request.getItemId()));
        WorkProcess workProcess = workProcessRepository.findByIdAndDeleteYnFalse(recycle.getWorkProcessId())
                .orElseThrow(() -> new NotFoundException("해당 공정이 존재하지 않습니다. 입력 id: " + recycle.getWorkProcessId()));
        List<LotMaster> usableLotList = lotMasterRepository.findBadLotByItemIdAndWorkProcess(request.getItemId(), recycle.getWorkProcessId());
        int createAmount = request.getAmount();
        LotMasterRequest lotRequest = new LotMasterRequest();
        lotRequest.setCreatedAmount(createAmount);
        lotRequest.setStockAmount(createAmount);
        lotRequest.setRecycleAmount(createAmount);
        lotRequest.setEnrollmentType(RECYCLE);
        lotRequest.setDummyYn(false);
        lotRequest.setItem(item);
        lotRequest.setWorkProcessDivision(workProcess.getWorkProcessDivision());

        LotMaster lotmaster = lotHelper.createLotMaster(lotRequest);

        for (LotMaster dbLotMaster:usableLotList) {
            if(createAmount <= dbLotMaster.getBadItemAmount()){
                dbLotMaster.setBadItemAmount(dbLotMaster.getBadItemAmount() - createAmount);
                createAmount = 0;
            }
            else if(createAmount > dbLotMaster.getBadItemAmount()){
                createAmount = createAmount - dbLotMaster.getBadItemAmount();
                dbLotMaster.setBadItemAmount(0);
            }
            lotMasterRepository.save(dbLotMaster);

            if(createAmount == 0){
                break;
            }
        }

        PopRecycleResponse response = lotMasterRepository.findBadAmountByWorkProcess(recycle.getWorkProcessId(), request.getItemId());
        return response;
    }
}
