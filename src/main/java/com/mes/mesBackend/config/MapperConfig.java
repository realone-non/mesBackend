package com.mes.mesBackend.config;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.mapper.MapperCustom;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        MapperCustom modelMapper = new MapperCustom();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addConverter(toEmpResponseConvert);
        modelMapper.addConverter(toUnitResponseConverter);
        modelMapper.addConverter(toGridResponseConvert);
        modelMapper.addConverter(toWorkCenterCheckDetailResponseConvert);
        modelMapper.addConverter(toItemFileResponseConvert);
        modelMapper.addConverter(toBomItemResponseConvert);
        modelMapper.addConverter(toItemBomResponseConvert);
        modelMapper.addConverter(toBomMasterResponseConvert);
        modelMapper.addConverter(toSubItemResponseConvert);
        modelMapper.addConverter(toWorkLineResponse);
        modelMapper.addConverter(toEstimateItemResponse);
        modelMapper.addConverter(toContractItemResponse);
        modelMapper.addConverter(contractToProduceOrderConverter);
        modelMapper.addConverter(contractItemToProduceOrderConverter);
        modelMapper.addConverter(equipmentToResponse);
        modelMapper.addConverter(toOutsourcingInputConverter);

        return modelMapper;
    }

    // UserResponse engNameAndPosition 영문이름+직위 매핑
    Converter<User, UserResponse> toEmpResponseConvert = new Converter<User, UserResponse>() {
        @Override
        public UserResponse convert(MappingContext<User, UserResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            User user = context.getSource();
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            userResponse.setEngNameAndPosition(user.getEngName() + " " + user.getPosition());
            return userResponse;
        }
    };

    // 소수점 3자리 까지 보이는 매핑
    Converter<Unit, UnitResponse> toUnitResponseConverter = new Converter<Unit, UnitResponse>() {
        @Override
        public UnitResponse convert(MappingContext<Unit, UnitResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            Unit unit = context.getSource();
            UnitResponse unitResponse = modelMapper.map(unit, UnitResponse.class);
            DecimalFormat df = new DecimalFormat("0.000");
            String result = df.format(unit.getBaseScale());
            unitResponse.setBaseScale(result);
            return unitResponse;
        }
    };

    // gridResponse의 colId 매핑
    Converter<GridOption, GridOptionResponse> toGridResponseConvert = new Converter<GridOption, GridOptionResponse>() {
        @Override
        public GridOptionResponse convert(MappingContext<GridOption, GridOptionResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            GridOption gridOption = context.getSource();
            GridOptionResponse gridOptionResponse = modelMapper.map(gridOption, GridOptionResponse.class);
            gridOptionResponse.setColId(gridOption.getHeader().getColumnName());
            return gridOptionResponse;
        }
    };

    // 작업장별 점검항목 세부 소수점 매핑
    Converter<WorkCenterCheckDetail, WorkCenterCheckDetailResponse> toWorkCenterCheckDetailResponseConvert = new Converter<WorkCenterCheckDetail, WorkCenterCheckDetailResponse>() {
        @Override
        public WorkCenterCheckDetailResponse convert(MappingContext<WorkCenterCheckDetail, WorkCenterCheckDetailResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            WorkCenterCheckDetail workCenterCheckDetail = context.getSource();
            WorkCenterCheckDetailResponse workCenterCheckDetailResponse = modelMapper.map(workCenterCheckDetail, WorkCenterCheckDetailResponse.class);
            DecimalFormat df = new DecimalFormat("0.000");
            String usl = df.format(workCenterCheckDetail.getUsl());
            String lsl = df.format(workCenterCheckDetail.getLsl());
            workCenterCheckDetailResponse.setUsl(usl);
            workCenterCheckDetailResponse.setLsl(lsl);
            return workCenterCheckDetailResponse;
        }
    };

    // 품목파일 날짜 포맷
    Converter<ItemFile, ItemFileResponse> toItemFileResponseConvert = new Converter<ItemFile, ItemFileResponse>() {
        @Override
        public ItemFileResponse convert(MappingContext<ItemFile, ItemFileResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            ItemFile itemFile = context.getSource();
            ItemFileResponse itemFileResponse = modelMapper.map(itemFile, ItemFileResponse.class);
            itemFileResponse.setCreatedDate(itemFile.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return itemFileResponse;
        }
    };

    // BomItemResponse의 price 매핑  단가*수량
    Converter<BomItemDetail, BomItemResponse> toBomItemResponseConvert = new Converter<BomItemDetail, BomItemResponse>() {
        @Override
        public BomItemResponse convert(MappingContext<BomItemDetail, BomItemResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            BomItemDetail bomItemDetail = context.getSource();
            BomItemResponse bomItemResponse = modelMapper.map(bomItemDetail, BomItemResponse.class);
            int price = bomItemResponse.getItem().getInputUnitPrice() * bomItemResponse.getAmount();
            bomItemResponse.setPrice(price);
            return bomItemResponse;
        }
    };

    Converter<Item, ItemResponse.itemToBomResponse> toItemBomResponseConvert = new Converter<Item, ItemResponse.itemToBomResponse>() {
        @Override
        public ItemResponse.itemToBomResponse convert(MappingContext<Item, ItemResponse.itemToBomResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            Item item = context.getSource();

            ItemResponse itemResponse = modelMapper.map(item, ItemResponse.class);
            ItemResponse.itemToBomResponse map = modelMapper.map(item, ItemResponse.itemToBomResponse.class);

            String account = itemResponse.getItemAccount().getAccount();

            map.setItemAccount(account);
            return map;
        }
    };

    Converter<BomMaster, BomMasterResponse> toBomMasterResponseConvert = new Converter<BomMaster, BomMasterResponse>() {
        @Override
        public BomMasterResponse convert(MappingContext<BomMaster, BomMasterResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            BomMaster bomMaster = context.getSource();
            BomMasterResponse bomMasterResponse = modelMapper.map(bomMaster, BomMasterResponse.class);

            bomMasterResponse.getItem().setClientName(null);
            bomMasterResponse.getItem().setUnitCodeName(null);
            bomMasterResponse.getItem().setStorageLocation(null);

            return bomMasterResponse;
        }
    };

    Converter<SubItem, SubItemResponse> toSubItemResponseConvert = new Converter<SubItem, SubItemResponse>() {
        @Override
        public SubItemResponse convert(MappingContext<SubItem, SubItemResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            SubItem subItem = context.getSource();
            SubItemResponse subItemResponse = modelMapper.map(subItem, SubItemResponse.class);

            subItemResponse.setItemId(subItem.getItem().getId());
            subItemResponse.setItemNo(subItem.getItem().getItemNo());
            subItemResponse.setItemName(subItem.getItem().getItemName());
            subItemResponse.setSubItemId(subItem.getSubItem().getId());
            subItemResponse.setSubItemNo(subItem.getSubItem().getItemNo());
            subItemResponse.setSubItemName(subItem.getSubItem().getItemName());
            return subItemResponse;
        }
    };

    Converter<WorkLine, WorkLineResponse.workLineAndWorkCenterAndWorkProcess> toWorkLineResponse = new Converter<WorkLine, WorkLineResponse.workLineAndWorkCenterAndWorkProcess>() {
        @Override
        public WorkLineResponse.workLineAndWorkCenterAndWorkProcess convert(MappingContext<WorkLine, WorkLineResponse.workLineAndWorkCenterAndWorkProcess> context) {
            ModelMapper modelMapper = new ModelMapper();
            WorkLine workLine = context.getSource();
            WorkLineResponse.workLineAndWorkCenterAndWorkProcess workLineCustomResponse = modelMapper.map(workLine, WorkLineResponse.workLineAndWorkCenterAndWorkProcess.class);
            workLineCustomResponse.setWorkLineName(workLine.getWorkLineName());
            workLineCustomResponse.setWorkCenterName(workLine.getWorkCenter().getWorkCenterName());
//            workLineCustomResponse.setWorkProcessName(workLine.getWorkProcess().getWorkProcessName());
            return workLineCustomResponse;
        }
    };

    Converter<EstimateItemDetail, EstimateItemResponse> toEstimateItemResponse = new Converter<EstimateItemDetail, EstimateItemResponse>() {
        @Override
        public EstimateItemResponse convert(MappingContext<EstimateItemDetail, EstimateItemResponse> context) {
            ModelMapper mapper = new ModelMapper();
            EstimateItemDetail itemDetail = context.getSource();
            EstimateItemResponse estimateItemResponse = mapper.map(itemDetail, EstimateItemResponse.class);
            int price = estimateItemResponse.getItem().getInputUnitPrice() * estimateItemResponse.getAmount();
            estimateItemResponse.setPrice(price);
            return estimateItemResponse;
        }
    };

    // 4-2. 수주 품목 등록
    Converter<ContractItem, ContractItemResponse> toContractItemResponse = new Converter<ContractItem, ContractItemResponse>() {
        @Override
        public ContractItemResponse convert(MappingContext<ContractItem, ContractItemResponse> context) {
            ModelMapper mapper = new ModelMapper();
            ContractItem contractItem = context.getSource();
            ContractItemResponse contractItemResponse = mapper.map(contractItem, ContractItemResponse.class);
            // 수주금액: 수량 * 단가
            int contractAmount = contractItem.getItem().getInputUnitPrice() * contractItem.getAmount();
            contractItemResponse.setContractAmount(contractAmount);
            // 수주금액(원화): 수량 * 단가
            contractItemResponse.setContractAmountWon(contractAmount);
            // 부가세 : 수주금액 * 0.1
            double surtax = contractAmount * 0.1;
            contractItemResponse.setSurtax(surtax);
            // 고객발주번호: 수주의 고객발주번호
            contractItemResponse.setClientOrderNo(contractItem.getContract().getClientOrderNo());
            return contractItemResponse;
        }
    };

    Converter<Contract, ContractResponse.toProduceOrder> contractToProduceOrderConverter = new Converter<Contract, ContractResponse.toProduceOrder>() {
        @Override
        public ContractResponse.toProduceOrder convert(MappingContext<Contract, ContractResponse.toProduceOrder> context) {
            ModelMapper mapper = new ModelMapper();
            Contract contract = context.getSource();
            ContractResponse.toProduceOrder produceOrder = mapper.map(contract, ContractResponse.toProduceOrder.class);
            produceOrder.setContractNo(contract.getContractNo());
            produceOrder.setCName(contract.getClient().getClientName());
            produceOrder.setPeriodDate(contract.getPeriodDate());
            return produceOrder;
        }
    };

    Converter<ContractItem, ContractItemResponse.toProduceOrder> contractItemToProduceOrderConverter = new Converter<ContractItem, ContractItemResponse.toProduceOrder>() {
        @Override
        public ContractItemResponse.toProduceOrder convert(MappingContext<ContractItem, ContractItemResponse.toProduceOrder> context) {
            ModelMapper mapper = new ModelMapper();
            ContractItem contractItem = context.getSource();
            ContractItemResponse.toProduceOrder produceOrder = mapper.map(contractItem, ContractItemResponse.toProduceOrder.class);
            produceOrder.setItemNo(contractItem.getItem().getItemNo());
            produceOrder.setItemName(contractItem.getItem().getItemName());
            produceOrder.setAmount(contractItem.getAmount());
            return produceOrder;
        }
    };


    Converter<Equipment, EquipmentResponse> equipmentToResponse = new Converter<Equipment, EquipmentResponse>() {
        @Override
        public EquipmentResponse convert(MappingContext<Equipment, EquipmentResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            Equipment equipment = context.getSource();
            EquipmentResponse response = modelMapper.map(equipment, EquipmentResponse.class);
            if (equipment.getWorkLine() != null) {
                response.setEquipmentType(equipment.getWorkLine().getWorkLineName());
            }
            return response;
        }
    };


    //LOT마스터 외주입고 LOT정보 변환
    Converter<LotMaster, OutsourcingInputLOTResponse> toOutsourcingInputConverter = new Converter<LotMaster, OutsourcingInputLOTResponse>() {
        @Override
        public OutsourcingInputLOTResponse convert(MappingContext<LotMaster, OutsourcingInputLOTResponse> context) {
            ModelMapper mapper = new ModelMapper();
            LotMaster lotMaster = context.getSource();
            OutsourcingInputLOTResponse response = mapper.map(lotMaster, OutsourcingInputLOTResponse.class);
            response.setInputAmount(lotMaster.getStockAmount());
            response.setTestRequestType(lotMaster.getOutSourcingInput().getTestRequestType());
            return response;
        }
    };
}
