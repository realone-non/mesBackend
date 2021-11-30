package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.WorkPlaceRequest;
import com.mes.mesBackend.dto.response.WorkPlaceResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.entity.WorkPlace;
import com.mes.mesBackend.entity.WorkPlaceBusinessType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WorkPlaceMappedRepository;
import com.mes.mesBackend.repository.WorkPlaceRepository;
import com.mes.mesBackend.service.BusinessTypeService;
import com.mes.mesBackend.service.WorkPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// 사업장
@Service
public class WorkPlaceServiceImpl implements WorkPlaceService {

    @Autowired WorkPlaceRepository workPlaceRepository;
    @Autowired BusinessTypeService businessTypeService;
    @Autowired WorkPlaceMappedRepository workPlaceMappedRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public WorkPlace getWorkPlaceOrThrow(Long id) throws NotFoundException {
        return workPlaceRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(()-> new NotFoundException("workPlace does not exists. input id:" + id));
    }

    // 사업장 생성
    public WorkPlaceResponse createWorkPlace(WorkPlaceRequest workPlaceRequest) throws NotFoundException {
        List<Long> getTypeIds = workPlaceRequest.getType();
        WorkPlace workPlace = modelMapper.toEntity(workPlaceRequest, WorkPlace.class);

        // create workPlace
        WorkPlace saveWorkPlace = workPlaceRepository.save(workPlace);

        // create workPlaceMapped
        List<WorkPlaceBusinessType> workPlaceMapped = createMapped(saveWorkPlace, getTypeIds);

        // workPlace에 workPlaceMapped 추가
        saveWorkPlace.setType(workPlaceMapped);
        workPlaceRepository.save(saveWorkPlace);
        return modelMapper.toResponse(saveWorkPlace, WorkPlaceResponse.class);
    }

    // BusinessType mapped 생성
    private List<WorkPlaceBusinessType> createMapped(WorkPlace workPlace, List<Long> businessTypeIds) throws NotFoundException {
        List<WorkPlaceBusinessType> workPlaceMappeds = new ArrayList<>();
        for (Long businessTypeId : businessTypeIds) {
            BusinessType findBusinessType = businessTypeService.getBusinessTypeOrThrow(businessTypeId);
            WorkPlaceBusinessType workPlaceMapped = new WorkPlaceBusinessType();
            // mapped 생성
            workPlaceMapped.setWorkPlace(workPlace);
            workPlaceMapped.setBusinessType(findBusinessType);
            workPlaceMappeds.add(workPlaceMappedRepository.save(workPlaceMapped));
        }
        return workPlaceMappeds;
    }

//    // 사업장 단일 조회
    public WorkPlaceResponse getWorkPlace(Long id) throws NotFoundException {
        WorkPlace workPlace = getWorkPlaceOrThrow(id);
        return modelMapper.toResponse(workPlace, WorkPlaceResponse.class);
    }

    // 사업장 전체 조회
    public List<WorkPlaceResponse> getWorkPlaces() {
        List<WorkPlace> workPlaces = workPlaceRepository.findAllByDeleteYnFalse();
        return modelMapper.toListResponses(workPlaces, WorkPlaceResponse.class);
    }

    // 사업장 페이징 조회
//    public Page<WorkPlaceResponse> getWorkPlaces(Pageable pageable) {
//        Page<WorkPlace> workPlaces = workPlaceRepository.findAllByDeleteYnFalse(pageable);
//        return modelMapper.toPageResponses(workPlaces, WorkPlaceResponse.class);
//    }

    // workPlaceMapped 모두 삭제
    private void deleteWorkPlaceMapped(WorkPlace workPlace) {
        workPlaceMappedRepository.deleteAllByWorkPlace(workPlace);
    }

    // 사업장 수정
    public WorkPlaceResponse updateWorkPlace(Long id, WorkPlaceRequest workPlaceRequest) throws NotFoundException {
        List<Long> newBusinessTypeIds = workPlaceRequest.getType();
        WorkPlace findWorkPlace = getWorkPlaceOrThrow(id);

        // delete mapped
        deleteWorkPlaceMapped(findWorkPlace);

        // update workPlace
        findWorkPlace.put(modelMapper.toEntity(workPlaceRequest, WorkPlace.class));
        WorkPlace newWorkPlace = workPlaceRepository.save(findWorkPlace);

        // create mapped
        newWorkPlace.setType(createMapped(findWorkPlace, newBusinessTypeIds));
        workPlaceRepository.save(newWorkPlace);

        return modelMapper.toResponse(newWorkPlace, WorkPlaceResponse.class);
    }

    // 사업장 삭제
    public void deleteWorkPlace(Long id) throws NotFoundException {
        WorkPlace workPlace = getWorkPlaceOrThrow(id);
        workPlace.delete();
        workPlaceRepository.save(workPlace);
    }
}
