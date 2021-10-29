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
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new MapperCustom();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addConverter(toWorkPlaceResponseConvert);
        modelMapper.addConverter(toFactoryResponseConvert);
        modelMapper.addConverter(toEmpResponseConvert);
        modelMapper.addConverter(toUnitResponseConverter);
        modelMapper.addConverter(toGridResponseConvert);
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

    // FactoryResponse workPlaceName 매핑
    Converter<Factory, FactoryResponse> toFactoryResponseConvert = new Converter<Factory, FactoryResponse>() {
        @Override
        public FactoryResponse convert(MappingContext<Factory, FactoryResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            Factory factory = context.getSource();
            FactoryResponse factoryResponse = modelMapper.map(factory, FactoryResponse.class);

            factoryResponse.setWorkPlaceName(factory.getWorkPlace().getWorkPlaceName());
            return factoryResponse;
        }
    };

    // EmployeeResponse engNameAndPosition 영문이름+직위 매핑
    Converter<User, EmployeeResponse> toEmpResponseConvert = new Converter<User, EmployeeResponse>() {
        @Override
        public EmployeeResponse convert(MappingContext<User, EmployeeResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            User user = context.getSource();
            EmployeeResponse employeeResponse = modelMapper.map(user, EmployeeResponse.class);
            employeeResponse.setDeptName(user.getDepartment().getDeptName());
            employeeResponse.setEngNameAndPosition(user.getEngName() + " " + user.getPosition());
            return employeeResponse;
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

}
