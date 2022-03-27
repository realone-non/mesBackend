package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.WorkPlaceRequest;
import com.mes.mesBackend.dto.response.WorkPlaceResponse;
import com.mes.mesBackend.entity.WorkPlace;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.WorkPlaceRepository;
import com.mes.mesBackend.service.WorkPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 사업장
@Service
@RequiredArgsConstructor
public class WorkPlaceServiceImpl implements WorkPlaceService {

    private final WorkPlaceRepository workPlaceRepository;
    private final ModelMapper modelMapper;


    @Override
    public WorkPlace getWorkPlaceOrThrow(Long id) throws NotFoundException {
        return workPlaceRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(()-> new NotFoundException("workPlace does not exists. input id:" + id));
    }

    // 사업장 생성
    public WorkPlaceResponse createWorkPlace(WorkPlaceRequest workPlaceRequest) {
        WorkPlace workPlace = modelMapper.toEntity(workPlaceRequest, WorkPlace.class);
        workPlaceRepository.save(workPlace);
        return modelMapper.toResponse(workPlace, WorkPlaceResponse.class);
    }

//    // 사업장 단일 조회
    public WorkPlaceResponse getWorkPlace(Long id) throws NotFoundException {
        WorkPlace workPlace = getWorkPlaceOrThrow(id);
        return modelMapper.toResponse(workPlace, WorkPlaceResponse.class);
    }

    // 사업장 전체 조회
    public List<WorkPlaceResponse> getWorkPlaces() {
        List<WorkPlace> workPlaces = workPlaceRepository.findAllByDeleteYnFalseOrderByCreatedDateDesc();
        return modelMapper.toListResponses(workPlaces, WorkPlaceResponse.class);
    }

    // 사업장 페이징 조회
//    public Page<WorkPlaceResponse> getWorkPlaces(Pageable pageable) {
//        Page<WorkPlace> workPlaces = workPlaceRepository.findAllByDeleteYnFalse(pageable);
//        return modelMapper.toPageResponses(workPlaces, WorkPlaceResponse.class);
//    }

    // 사업장 수정
    public WorkPlaceResponse updateWorkPlace(Long id, WorkPlaceRequest workPlaceRequest) throws NotFoundException {
        WorkPlace findWorkPlace = getWorkPlaceOrThrow(id);
        // update workPlace
        findWorkPlace.put(modelMapper.toEntity(workPlaceRequest, WorkPlace.class));
        WorkPlace newWorkPlace = workPlaceRepository.save(findWorkPlace);
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
