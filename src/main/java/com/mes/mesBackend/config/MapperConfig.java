package com.mes.mesBackend.config;

import com.mes.mesBackend.dto.response.*;
import com.mes.mesBackend.entity.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addConverter(toWorkPlaceResponseConvert);
        modelMapper.addConverter(toFactoryResponseConvert);
        modelMapper.addConverter(toEmpResponseConvert);
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
    Converter<Employee, EmployeeResponse> toEmpResponseConvert = new Converter<Employee, EmployeeResponse>() {
        @Override
        public EmployeeResponse convert(MappingContext<Employee, EmployeeResponse> context) {
            ModelMapper modelMapper = new ModelMapper();
            Employee employee = context.getSource();
            EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);
            employeeResponse.setDeptName(employee.getDepartment().getDeptName());
            employeeResponse.setEngNameAndPosition(employee.getEngName() + " " + employee.getPosition());
            return employeeResponse;
        }
    };
}
