package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.OutsourcingMaterialReleaseRequest;
import com.mes.mesBackend.dto.request.OutsourcingProductionRequestRequest;
import com.mes.mesBackend.dto.response.OutsourcingMaterialReleaseResponse;
import com.mes.mesBackend.dto.response.OutsourcingProductionResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.BomItemDetailRepository;
import com.mes.mesBackend.repository.BomMasterRepository;
import com.mes.mesBackend.repository.OutSourcingProductionRawMaterialOutputInfoRepository;
import com.mes.mesBackend.repository.OutSourcingProductionRequestRepository;
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
    BomMasterRepository bomMasterRepository;
    @Autowired
    BomItemDetailRepository bomItemDetailRepository;

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
    public Optional<OutsourcingMaterialReleaseResponse> createOutsourcingMaterial(OutsourcingMaterialReleaseRequest outsourcingMaterialReleaseRequest){
        OutSourcingProductionRawMaterialOutputInfo materialOutputInfo = modelMapper.toEntity(outsourcingMaterialReleaseRequest, OutSourcingProductionRawMaterialOutputInfo.class);
        materialOutputInfo.setBomItemDetail(bomItemDetailRepository.getById(outsourcingMaterialReleaseRequest.getItemDetailId()));
        materialOutputInfo.setOutSourcingProductionRequest(outsourcingProductionRepository.getById(outsourcingMaterialReleaseRequest.getRequestId()));
        outsourcingMaterialRepository.save(materialOutputInfo);
        return outsourcingMaterialRepository.findByMaterialId(materialOutputInfo.getId());
    }

    //외주생산 원재료 출고 대상 리스트 조회
    public List<OutsourcingMaterialReleaseResponse> getOutsourcingMeterials(Long productionId){
        return outsourcingMaterialRepository.findAllUseYn(productionId);
    }

    //외주생산 원재료 출고 대상 단일 조회
    public Optional<OutsourcingMaterialReleaseResponse> getOutsourcingMaterial(Long materialId){
        return outsourcingMaterialRepository.findByMaterialId(materialId);
    }

    //외주생산 원재료 출고 대상 수정
    public Optional<OutsourcingMaterialReleaseResponse> modifyOutsourcingMaterial(Long materialId, OutsourcingMaterialReleaseRequest request){
        OutSourcingProductionRawMaterialOutputInfo info = outsourcingMaterialRepository.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("not found data"));
        BomItemDetail itemDetail = bomItemDetailRepository.getById(request.getItemDetailId());
        info.update(request, itemDetail);
        outsourcingMaterialRepository.save(info);
        return outsourcingMaterialRepository.findByMaterialId(materialId);
    }

    //외주생산 원재료 출고 대상 삭제
    public void deleteOutsourcingMaterial(Long id){
        OutSourcingProductionRawMaterialOutputInfo info = outsourcingMaterialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found data"));
        info.delete();
        outsourcingMaterialRepository.save(info);
    }
}
