package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.WorkPlaceRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.dto.response.WorkPlaceResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.entity.WorkPlace;
import com.mes.mesBackend.entity.WorkPlaceMapped;
import com.mes.mesBackend.repository.WorkPlaceMappedRepository;
import com.mes.mesBackend.repository.WorkPlaceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// 사업장
@Service
public class WorkPlaceServiceImpl implements WorkPlaceService {

    @Autowired WorkPlaceRepository workPlaceRepository;
    @Autowired BusinessTypeService businessTypeService;
    @Autowired WorkPlaceMappedRepository workPlaceMappedRepository;
    @Autowired ModelMapper modelMapper;

    // 사업장 생성
    public WorkPlaceResponse createWorkPlace(WorkPlaceRequest workPlaceRequest) {
        List<Long> businessTypeIds = workPlaceRequest.getType();
        WorkPlace workPlace = toEntity(workPlaceRequest);
        WorkPlace saveWorkPlace = workPlaceRepository.save(workPlace);
        List<WorkPlaceMapped> workPlaceMappeds = createWorkPlaceAndBusinessTypeMapped(saveWorkPlace, businessTypeIds);
        return toResponse(saveWorkPlace,workPlaceMappeds);
    }

    // BusinessType mapped 생성
    private List<WorkPlaceMapped> createWorkPlaceAndBusinessTypeMapped(WorkPlace workPlace, List<Long> businessTypeIds) {
        List<WorkPlaceMapped> workPlaceMappeds = new ArrayList<>();
        for (Long businessTypeId : businessTypeIds) {
            BusinessType findBusinessType = businessTypeService.findBusinessTypeByIdAndDeleteYn(businessTypeId);
            WorkPlaceMapped workPlaceMapped = new WorkPlaceMapped();
            workPlaceMapped.setWorkPlace(workPlace);
            workPlaceMapped.setBusinessType(findBusinessType);
            workPlaceMappeds.add(workPlaceMappedRepository.save(workPlaceMapped));
        }
        return workPlaceMappeds;
    }

    // 사업장 단일 조회
    public WorkPlaceResponse getWorkPlace(Long id) {
        WorkPlace findWorkPlace = workPlaceRepository.findByIdAndDeleteYnFalse(id);
        return toResponse(findWorkPlace, findWorkPlace.getType());
    }

    // 사업장 페이징 조회
//    public Page<WorkPlaceResponse> getWorkPlaces(Pageable pageable) {
//        Page<WorkPlace> workPlaces = workPlaceRepository.findAllByDeleteYnFalse(pageable);
//        return toResponses(workPlaces);
//    }

    // 사업장 수정
    public WorkPlaceResponse updateWorkPlace(Long id, WorkPlaceRequest workPlaceRequest) {
        WorkPlace findWorkPlace = workPlaceRepository.findByIdAndDeleteYnFalse(id);
        toEntity(workPlaceRequest);
        return  null;
    }

    // 사업장 삭제
    public void deleteWorkPlace(Long id) {

    }

    // Entity -> Response
    private WorkPlaceResponse toResponse(WorkPlace workPlace, List<WorkPlaceMapped> workPlaceMappeds) {
        List<BusinessTypeResponse> businessTypeResponses = new ArrayList<>();
        for (WorkPlaceMapped workPlaceMapped : workPlaceMappeds) {
            BusinessTypeResponse findBusinessType = businessTypeService.getBusinessType(workPlaceMapped.getBusinessType().getId());
            businessTypeResponses.add(findBusinessType);
        }
        WorkPlaceResponse workPlaceResponse = modelMapper.map(workPlace, WorkPlaceResponse.class);
        workPlaceResponse.setType(businessTypeResponses);
        return workPlaceResponse;
    }

    // Request -> Entity
    private WorkPlace toEntity(WorkPlaceRequest workPlaceRequest) {
        return modelMapper.map(workPlaceRequest, WorkPlace.class);
    }

////     Page<Entity> -> Page<Response>
//    private Page<WorkPlaceResponse> toResponses(Page<WorkPlace> workPlaces) {
////        return workPlaces.map(workPlace -> modelMapper.typeMap(workPlace, WorkPlaceResponse.class).)
//    }
//
//    private void ddd(Page<WorkPlace> workPlaces) {
//        modelMapper.typeMap(BusinessType.class, BusinessTypeResponse.class).addMappings(mapping -> {
//            mapping.map(BusinessType::getName, BusinessTypeResponse::setName);
//            mapping.map(BusinessType::get, BusinessTypeResponse::setUseYn);
//        })
//    }
}
