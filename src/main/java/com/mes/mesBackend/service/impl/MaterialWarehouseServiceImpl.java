package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.MaterialStockInspectRequestRequest;
import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.dto.request.RequestMaterialStockInspect;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.AmountHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.MaterialWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialWarehouseServiceImpl implements MaterialWarehouseService {
    @Autowired
    ItemLogRepository itemLogRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    WareHouseRepository wareHouseRepository;
    @Autowired
    ItemAccountRepository itemAccountRepository;
    @Autowired
    MaterialStockInspectRequestRepository materialStockInspectRequestRepository;
    @Autowired
    AmountHelper amountHelper;
    @Autowired
    MaterialStockInspectRepository materialStockInspectRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LotMasterRepository lotMasterRepository;

    //수불부 조회
    public List<ReceiptAndPaymentResponse> getReceiptAndPaymentList(Long warehouseId, Long itemAccountId, LocalDate fromDate, LocalDate toDate){
        return itemLogRepository.findAllConditionResponse(warehouseId, itemAccountId, fromDate, toDate, false);
    }

    //재고실사의뢰 등록
    public MaterialStockInspectRequestResponse createMaterialStockInspectRequest(MaterialStockInspectRequestRequest request) throws NotFoundException{
        MaterialStockInspectRequest dbStockInspect = modelMapper.toEntity(request, MaterialStockInspectRequest.class);
        if(request.getItemAccountId() != null){
            ItemAccount itemAccount = itemAccountRepository.findByIdAndDeleteYnFalse(request.getItemAccountId())
                    .orElseThrow(() -> new NotFoundException("itemAccount does not exist. input id: " + request.getWarehouseId()));
            dbStockInspect.setItemAccount(itemAccount);
        }
        WareHouse wareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("warehouse does not exist. input id: " + request.getWarehouseId()));
        dbStockInspect.setWareHouse(wareHouse);
        materialStockInspectRequestRepository.save(dbStockInspect);
        System.out.println(dbStockInspect.getId());
        return materialStockInspectRequestRepository.findByIdAndDeleteYn(dbStockInspect.getId())
                .orElseThrow(() -> new NotFoundException("stockInspectRequest does not exist. input id: " + dbStockInspect.getId()));
    }
    //재고실사의뢰 조회
    public List<MaterialStockInspectRequestResponse> getMaterialStockInspectRequestList(LocalDate fromDate, LocalDate toDate){
        return materialStockInspectRequestRepository.findAllByCondition(fromDate, toDate);
    }
    //재고실사의뢰 단건조회
    public MaterialStockInspectRequestResponse getMaterialStockInspectRequest(Long id) throws NotFoundException{
        return materialStockInspectRequestRepository.findByIdAndDeleteYn(id)
                .orElseThrow(() -> new NotFoundException("stockInspectRequest does not exist. input id: " + id));
    }
    //재고실사의뢰 수정
    public MaterialStockInspectRequestResponse modifyMaterialStockInspect(Long id, MaterialStockInspectRequestRequest request) throws NotFoundException{
        MaterialStockInspectRequest dbStockRequest = materialStockInspectRequestRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("stockInspect does not exist. input id: " + id));
        WareHouse wareHouse = wareHouseRepository.findByIdAndDeleteYnFalse(request.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("warehouse does not exist. input id: " + request.getWarehouseId()));
        ItemAccount itemAccount = itemAccountRepository.findByIdAndDeleteYnFalse(request.getItemAccountId())
                .orElseThrow(() -> new NotFoundException("itemAccount does not exist. input id: " + request.getItemAccountId()));
        dbStockRequest.update(request.getNote(), wareHouse, itemAccount);
        materialStockInspectRequestRepository.save(dbStockRequest);
        return materialStockInspectRequestRepository.findByIdAndDeleteYn(id)
                .orElseThrow(() -> new NotFoundException("stockInspectRequest does not exist. input id: " + id));
    }

    //재고실사의뢰 삭제
    public void deleteMaterialStockInspectRequest(Long id) throws NotFoundException{
        MaterialStockInspectRequest dbStockRequest = materialStockInspectRequestRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("stockInspect does not exist. input id: " + id));
        dbStockRequest.delete(id);
        materialStockInspectRequestRepository.save(dbStockRequest);
    }

    //재고실사 조회
    public List<MaterialStockInspectResponse> getMaterialStockInspects(Long requestId, LocalDate fromDate, LocalDate toDate, String itemAccount){
        return materialStockInspectRepository.findAllByCondition(requestId, fromDate, toDate, itemAccount);
    }
    //재고조사 단일 조회
    public MaterialStockInspectResponse getMaterialStockInspect(Long requestId, Long id) throws NotFoundException {
        MaterialStockInspectRequest inspectRequest = materialStockInspectRequestRepository.findByIdAndDeleteYnFalse(requestId)
                .orElseThrow(() -> new NotFoundException("stockInspectRequest does not exist. input id: " + requestId));
        return materialStockInspectRepository.findByIdAndDeleteYn(id)
                .orElseThrow(() -> new NotFoundException("stockInspect does not exist. input id: " + id));
    }

    //DB재고실사 데이터 등록
    public void createMaterialStockInspect (Long requestId, Long itemAccountId) throws NotFoundException {
        List<MaterialStockInspect> dbInspect = materialStockInspectRepository.findInspectFromDB(itemAccountId);
        MaterialStockInspectRequest request = materialStockInspectRequestRepository.findByIdAndDeleteYnFalse(requestId)
                .orElseThrow(() -> new NotFoundException("stockInspect does not exist. input id: " + requestId));
        for (MaterialStockInspect inspect: dbInspect) {
            inspect.setMaterialStockInspectRequest(request);
        }
        materialStockInspectRepository.saveAll(dbInspect);
        request.setInspectDate(LocalDate.now());
        materialStockInspectRequestRepository.save(request);
    }

    //재고조사 수정
    public List<MaterialStockInspectResponse> modifyMaterialStockInspect(Long requestId, List<RequestMaterialStockInspect> requestList)
            throws NotFoundException {
        MaterialStockInspectRequest dbRequest = materialStockInspectRequestRepository.findByIdAndDeleteYnFalse(requestId)
                .orElseThrow(() -> new NotFoundException("inspectRequest does not exist. input id: " + requestId));
        List<MaterialStockInspect> dbInspect = materialStockInspectRepository.findAllByDeleteYnFalseAndMaterialStockInspectRequest(dbRequest);

        for (RequestMaterialStockInspect request:requestList) {
            for (MaterialStockInspect inspect: dbInspect) {
                if(request.getId().equals(inspect.getId())){
                    inspect.update(request);
                    break;
                }
            }
        }
        materialStockInspectRepository.saveAll(dbInspect);
        return materialStockInspectRepository.findAllByCondition(requestId, null, null, null);
    }

    //재고조사 삭제
    public void deleteMaterialStockInspect(Long id) throws NotFoundException {
        MaterialStockInspect dbInsepct = materialStockInspectRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("stockInspect does not exist. input id: " + id));
        dbInsepct.delete();
        materialStockInspectRepository.save(dbInsepct);
    }

    //재고실사 승인 등록
    public List<MaterialStockInspectResponse> createStockInspectApproval(Long requestId, Long userId) throws NotFoundException {
        MaterialStockInspectRequest dbRequest = materialStockInspectRequestRepository.findByIdAndDeleteYnFalse(requestId)
                .orElseThrow(() -> new NotFoundException("inspectRequest does not exist. input id: " + requestId));
        List<MaterialStockInspect> dbInspect = materialStockInspectRepository.findAllByDeleteYnFalseAndMaterialStockInspectRequest(dbRequest);
        User user = userRepository.findByIdAndDeleteYnFalse(userId)
                .orElseThrow(() -> new NotFoundException("user does not exist. input id: " + userId));
        for (MaterialStockInspect inspect: dbInspect) {
                inspect.approval(user);
        }

        materialStockInspectRepository.saveAll(dbInspect);
        return materialStockInspectRepository.findAllByCondition(requestId, null, null, null);
    }

    //재고현황 조회
    public List<MaterialStockReponse> getMaterialStock(Long itemAccountId, Long itemId, Long itemAccoutCodeId, Long warehouseId){
        return lotMasterRepository.findStockByItemAccountAndItemAndItemAccountCode(
                itemAccountId, itemId, itemAccoutCodeId, warehouseId);
    }

    //헤더용 창고 목록 조회
    public List<HeaderWarehouseResponse> getHeaderWarehouse(){
        List<WareHouse> wareHouseList = wareHouseRepository.findAllByDeleteYnFalse();
        return modelMapper.toListResponses(wareHouseList, HeaderWarehouseResponse.class);
    }
}
