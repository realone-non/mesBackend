package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.WorkPlaceRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.dto.response.WorkPlaceResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.entity.WorkPlace;
import com.mes.mesBackend.entity.WorkPlaceMapped;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.WorkPlaceMappedRepository;
import com.mes.mesBackend.repository.WorkPlaceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// 사업장
@Service
public class WorkPlaceServiceImpl implements WorkPlaceService {

    @Autowired WorkPlaceRepository workPlaceRepository;
    @Autowired BusinessTypeService businessTypeService;
    @Autowired WorkPlaceMappedRepository workPlaceMappedRepository;
    @Autowired ModelMapper modelMapper;
    @Autowired Mapper mapper;

    // 사업장 생성
    public WorkPlaceResponse createWorkPlace(WorkPlaceRequest workPlaceRequest) {
        List<Long> getTypeIds = workPlaceRequest.getType();
        WorkPlace workPlace = mapper.toEntity(workPlaceRequest, WorkPlace.class);

        // create workPlace
        WorkPlace saveWorkPlace = workPlaceRepository.save(workPlace);

        // create workPlaceMapped
        List<WorkPlaceMapped> workPlaceMapped = createMapped(saveWorkPlace, getTypeIds);

        // workPlace에 workPlaceMapped 추가
        saveWorkPlace.setType(workPlaceMapped);
        workPlaceRepository.save(saveWorkPlace);
        return mapper.toResponse(saveWorkPlace, WorkPlaceResponse.class);
    }

    // BusinessType mapped 생성
    private List<WorkPlaceMapped> createMapped(WorkPlace workPlace, List<Long> businessTypeIds) {
        List<WorkPlaceMapped> workPlaceMappeds = new ArrayList<>();
        for (Long businessTypeId : businessTypeIds) {
            BusinessType findBusinessType = businessTypeService.findBusinessTypeByIdAndDeleteYn(businessTypeId);
            WorkPlaceMapped workPlaceMapped = new WorkPlaceMapped();
            // mapped 생성
            workPlaceMapped.setWorkPlace(workPlace);
            workPlaceMapped.setBusinessType(findBusinessType);
            workPlaceMappeds.add(workPlaceMappedRepository.save(workPlaceMapped));
        }
        return workPlaceMappeds;
    }

//    // 사업장 단일 조회
    public WorkPlaceResponse getWorkPlace(Long id) {
        WorkPlace workPlace = workPlaceRepository.findByIdAndDeleteYnFalse(id);
        return mapper.toResponse(workPlace, WorkPlaceResponse.class);
    }

    // 사업장 페이징 조회
    public Page<WorkPlaceResponse> getWorkPlaces(Pageable pageable) {
        Page<WorkPlace> workPlaces = workPlaceRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(workPlaces, WorkPlaceResponse.class);
    }

    // workPlaceMapped 모두 삭제
    private void deleteWorkPlaceMapped(WorkPlace workPlace) {
        workPlaceMappedRepository.deleteAllByWorkPlace(workPlace);
    }

    // 사업장 수정
    public WorkPlaceResponse updateWorkPlace(Long id, WorkPlaceRequest workPlaceRequest) {
        List<Long> newBusinessTypeIds = workPlaceRequest.getType();
        WorkPlace findWorkPlace = workPlaceRepository.findByIdAndDeleteYnFalse(id);
        // delete mapped
        deleteWorkPlaceMapped(findWorkPlace);

        // update workPlace
        findWorkPlace.put(mapper.toEntity(workPlaceRequest, WorkPlace.class));
        WorkPlace newWorkPlace = workPlaceRepository.save(findWorkPlace);

        // create mapped
        newWorkPlace.setType(createMapped(findWorkPlace, newBusinessTypeIds));
        workPlaceRepository.save(newWorkPlace);

        return mapper.toResponse(newWorkPlace, WorkPlaceResponse.class);
    }

    // 사업장 삭제
    public void deleteWorkPlace(Long id) {
        WorkPlace workPlace = workPlaceRepository.findByIdAndDeleteYnFalse(id);
        workPlace.setDeleteYn(true);
        workPlaceRepository.save(workPlace);
    }
}
