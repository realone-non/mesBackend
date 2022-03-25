package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.MaterialStockInspectRequestRequest;
import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.dto.request.RequestMaterialStockInspect;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.AmountHelper;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.MaterialWarehouseService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
    @Autowired
    WorkOrderDetailRepository workOrderDetailRepository;
    @Autowired
    BomMasterRepository bomMasterRepository;
    @Autowired
    BomItemDetailRepository bomItemDetailRepository;
    @Autowired
    ProduceOrderRepository produceOrderRepository;
    @Autowired
    PurchaseRequestRepository purchaseRequestRepository;

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
    public void deleteMaterialStockInspect(Long requestId, Long inspectId) throws NotFoundException {
        MaterialStockInspectRequest dbRequest = materialStockInspectRequestRepository.findByIdAndDeleteYnFalse(requestId)
                .orElseThrow(() -> new NotFoundException("inspectReqiest does not exist. input id: " + requestId));
        MaterialStockInspect dbInsepct = materialStockInspectRepository.findByIdAndDeleteYnFalse(inspectId)
                .orElseThrow(() -> new NotFoundException("stockInspect does not exist. input id: " + inspectId));
        dbInsepct.delete();
        materialStockInspectRepository.save(dbInsepct);
    }

    //재고실사 승인 등록
    public List<MaterialStockInspectResponse> createStockInspectApproval(Long requestId, String userCode) throws NotFoundException {
        MaterialStockInspectRequest dbRequest = materialStockInspectRequestRepository.findByIdAndDeleteYnFalse(requestId)
                .orElseThrow(() -> new NotFoundException("inspectRequest does not exist. input id: " + requestId));
        List<MaterialStockInspect> dbInspect = materialStockInspectRepository.findAllByDeleteYnFalseAndMaterialStockInspectRequest(dbRequest);
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new NotFoundException("user does not exist. input userCode: " + userCode));
        for (MaterialStockInspect inspect: dbInspect) {
                inspect.approval(user);
        }

        materialStockInspectRepository.saveAll(dbInspect);
        return materialStockInspectRepository.findAllByCondition(requestId, null, null, null);
    }

    //재고현황 조회
    public JSONArray getMaterialStock(Long itemGroupId, Long itemAccountId, String itemNo, String itemName, Long warehouseId){
        List<Item> items = itemRepository.findAllByCondition(itemGroupId, itemAccountId, itemNo, itemName, null);
        JSONArray materialStocks = new JSONArray();

        for (Item item:items) {
            int sum = 0;
            List<MaterialStockReponse> responseList = lotMasterRepository.findStockAmountByItemId(item.getId(), warehouseId);
            JSONObject itemAmount = new JSONObject();
            itemAmount.put("itemNo", item.getItemNo());
            itemAmount.put("itemName", item.getItemName());
            for (MaterialStockReponse response:responseList) {
                itemAmount.put(response.getWarehouseId().toString(), response.getAmount());
                itemAmount.put("inputUnitPrice", response.getInputUnitPrice());
                sum = sum + response.getAmount();
            }
            itemAmount.put("sum", sum);
            if(itemAmount.get("inputUnitPrice") != null){
                itemAmount.put("stockPrice", sum * (Integer.parseInt(itemAmount.get("inputUnitPrice").toString())));
            }
            materialStocks.add(itemAmount);
        }
        
        return materialStocks;
    }

    //헤더용 창고 목록 조회
    public JSONArray getHeaderWarehouse(){
        List<WareHouse> wareHouseList = wareHouseRepository.findAllByDeleteYnFalse();
        JSONArray headerList = new JSONArray();
        int seq = 3;
        for (WareHouse wareHouse:wareHouseList) {
            JSONObject header = new JSONObject();
            header.put("header", wareHouse.getWareHouseName());
            header.put("columnName", wareHouse.getId());
            header.put("seq", seq);
            headerList.add(header);
            seq++;
        }

        return headerList;
    }

    //Shortage조회
    public List<ShortageReponse> getShortage(Long itemGroupId, String itemNoAndName, LocalDate stdDate) throws NotFoundException {
        if(stdDate == null){
            stdDate = LocalDate.now();
        }
        List<ShortageReponse> shortageResponseList = new ArrayList<>();
        List<Item> itemList = itemRepository.findAllItemByAccount("원자재", "부자재", itemGroupId, itemNoAndName);
        List<WorkOrderDetail> workOrderDetails = workOrderDetailRepository.findByWorkDate(stdDate);
        for (Item item:itemList) {
            ShortageReponse shortageResponse = new ShortageReponse();
            float workAmount = 0;
            float sumAmount = 0;
            boolean isEmpty = false;
            for (WorkOrderDetail detail:workOrderDetails) {
//                ProduceOrder order = produceOrderRepository.findByIdAndDeleteYnFalse(detail.getProduceOrder().getId())
//                        .orElseGet(ProduceOrder::new);

                ProduceOrder order = produceOrderRepository.findByIdforShortage(detail.getProduceOrder().getId());
                if(order.getId().equals(null)){
                    continue;
                }
                List<ProduceOrderDetailResponse> detailList = produceOrderRepository.findAllProduceOrderDetail(order.getContractItem().getItem().getId());
                for (ProduceOrderDetailResponse detailResponse: detailList) {
                    if(detailResponse.getItemAccount().equals("반제품")){
                        workAmount = getDetailRecursive(detailResponse, item.getId(), workAmount);
                        sumAmount = sumAmount + workAmount;
                    }
                    else if(detailResponse.getItemId().equals(item.getId())){
                        workAmount = (int) (detailResponse.getBomAmount() * detail.getOrderAmount());
                        sumAmount = sumAmount + workAmount;
                    }
                }
            }
            Tuple scheduleInputAmount = purchaseRequestRepository.findItemByItemAndDateForShortage(item.getId(), LocalDate.now());
            shortageResponse.setProductionCapacity(sumAmount);
            shortageResponse.setMaterialNo(item.getItemNo());
            shortageResponse.setMaterialName(item.getItemName());
            if(scheduleInputAmount != null){
                shortageResponse.setScheduleInputAmount(scheduleInputAmount.get(1, Integer.class));
            }
            else{
                shortageResponse.setScheduleInputAmount(0);
            }
            ItemLog beforeAmount = itemLogRepository.findByItemIdAndWareHouseAndBeforeDayGroupBy(item.getId(), null, LocalDate.now().minusDays(1), null);
            if(beforeAmount == null){
                shortageResponse.setBeforeDayAmount(0);
            }
            else{
                shortageResponse.setBeforeDayAmount(beforeAmount.getBeforeDayStockAmount());
            }

            shortageResponse.setOverLackAmount(
                    shortageResponse.getBeforeDayAmount() +
                        shortageResponse.getScheduleInputAmount() -
                        shortageResponse.getProductionCapacity());
            shortageResponseList.add(shortageResponse);
        }
        return shortageResponseList;
    }

    public float getDetailRecursive(ProduceOrderDetailResponse response, Long itemId, float workAmount){
        List<ProduceOrderDetailResponse> detailList = produceOrderRepository.findAllProduceOrderDetail(response.getItemId());
        for (ProduceOrderDetailResponse detail:detailList) {
            if(detail.getItemAccount().equals("반제품")){
                workAmount = getDetailRecursive(detail, itemId, workAmount);
            }
            else if(detail.getItemId().equals(itemId)){
               workAmount = Float.parseFloat(String.valueOf(workAmount + detail.getBomAmount()));
            }
        }

        return workAmount;
    }
}
