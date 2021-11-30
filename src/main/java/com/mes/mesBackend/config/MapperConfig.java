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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        MapperCustom modelMapper = new MapperCustom();

//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setSkipNullEnabled(true);
//        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addConverter(toWorkPlaceResponseConvert);
        modelMapper.addConverter(toEmpResponseConvert);
        modelMapper.addConverter(toUnitResponseConverter);
        modelMapper.addConverter(toGridResponseConvert);
        modelMapper.addConverter(toWorkCenterCheckDetailResponseConvert);
        modelMapper.addConverter(toItemFileResponseConvert);
        modelMapper.addConverter(toBomItemResponseConvert);
        modelMapper.addConverter(toItemBomResponseConvert);
        modelMapper.addConverter(toBomMasterResponseConvert);
        modelMapper.addConverter(toSubItemResponseConvert);
        modelMapper.addConverter(toWorkDocumentConvert);
        modelMapper.addConverter(toWorkLineResponse);
        modelMapper.addConverter(toEstimateItemResponse);

        return modelMapper;
    }

    // workPlace type 매핑을 위한 modelMapper converter
    Converter<WorkPlace, WorkPlaceResponse> toWorkPlaceResponseConvert = new Converter<WorkPlace, WorkPlaceResponse>() {
        @Override
        public WorkPlaceResponse convert(MappingContext<WorkPlace, WorkPlaceResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            WorkPlace workPlace = context.getSource();
            WorkPlaceResponse workPlaceResponse = modelMapper.map(workPlace, WorkPlaceResponse.class);
            List<BusinessTypeResponse> businessTypeResponses = new ArrayList<>();

            for (WorkPlaceBusinessType workPlaceMapped : workPlace.getType()) {
                BusinessType businessType = workPlaceMapped.getBusinessType();
                BusinessTypeResponse businessTypeResponse = modelMapper.map(businessType, BusinessTypeResponse.class);
                businessTypeResponses.add(businessTypeResponse);
            }
            workPlaceResponse.setType(businessTypeResponses);
            return workPlaceResponse;
        }
    };

    // UserResponse engNameAndPosition 영문이름+직위 매핑
    Converter<User, UserResponse> toEmpResponseConvert = new Converter<User, UserResponse>() {
        @Override
        public UserResponse convert(MappingContext<User, UserResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            User user = context.getSource();
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
//            userResponse.setDeptName(user.getDepartment().getDeptName());
//            userResponse.setDepartment(user.getDepartment());
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

    Converter<WorkDocument, WorkDocumentResponse> toWorkDocumentConvert = new Converter<WorkDocument, WorkDocumentResponse>() {
        @Override
        public WorkDocumentResponse convert(MappingContext<WorkDocument, WorkDocumentResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            WorkDocument workDocument = context.getSource();
            WorkDocumentResponse workDocumentResponse = modelMapper.map(workDocument, WorkDocumentResponse.class);
            workDocumentResponse.setWorkProcess(workDocument.getWorkProcess().getWorkProcessName());
            workDocumentResponse.setWorkLine(workDocument.getWorkLine().getWorkLineName());
            workDocumentResponse.setItemId(workDocument.getItem().getId());
            workDocumentResponse.setItemNo(workDocument.getItem().getItemNo());
            workDocumentResponse.setItemName(workDocument.getItem().getItemName());
            return workDocumentResponse;
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
            workLineCustomResponse.setWorkProcessName(workLine.getWorkProcess().getWorkProcessName());
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
}
