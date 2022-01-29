package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.RecycleRequest;
import com.mes.mesBackend.dto.response.RecycleResponse;
import com.mes.mesBackend.entity.Recycle;
import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.RecycleRepository;
import com.mes.mesBackend.repository.WorkProcessRepository;
import com.mes.mesBackend.service.RecycleService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecycleServiceImpl implements RecycleService {

    @Autowired
    RecycleRepository recycleRepository;

    @Autowired
    WorkProcessRepository workProcessRepository;

    @Autowired
    ModelMapper mapper;

    //재사용 생성
    public RecycleResponse createRecycle(RecycleRequest request) throws NotFoundException {
        Recycle recycle = mapper.toEntity(request, Recycle.class);
        WorkProcess process = workProcessRepository.findByIdAndDeleteYnFalse(request.getWorkProcessId())
                .orElseThrow(() -> new NotFoundException("inspectRequest does not exist. input id: " + request.getWorkProcessId()));
        recycle.setWorkProcess(process);
        recycleRepository.save(recycle);
        RecycleResponse response = recycleRepository.findByIdAndDeleteYn(recycle.getId())
                .orElseThrow(() -> new NotFoundException("recycle does not exist. input id: " + request.getWorkProcessId()));
        return response;
    }

    //재사용 조회
    public RecycleResponse getRecycle(Long id) throws NotFoundException {
        RecycleResponse response = recycleRepository.findByIdAndDeleteYn(id)
                .orElseThrow(() -> new NotFoundException("inspectRequest does not exist. input id: " + id));
        return response;
    }

    //재사용 리스트 조회
    public List<RecycleResponse> getRecycles(Long workProcessId){
        return recycleRepository.findRecycles(workProcessId);
    }

    //재사용 수정
    public RecycleResponse modifyRecycle(Long id, RecycleRequest request) throws NotFoundException {
        Recycle dbRecycle = recycleRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("inspectRequest does not exist. input id: " + id));
        WorkProcess workProcess = workProcessRepository.findByIdAndDeleteYnFalse(request.getWorkProcessId())
                .orElseThrow(() -> new NotFoundException("inspectRequest does not exist. input id: " + request.getWorkProcessId()));
        dbRecycle.setWorkProcess(workProcess);
        dbRecycle.update(request);
        recycleRepository.save(dbRecycle);
        return recycleRepository.findByIdAndDeleteYn(id)
                .orElseThrow(() -> new NotFoundException("inspectRequest does not exist. input id: " + id));
    }

    //재사용 삭제
    public void deleteRecycle(Long id) throws NotFoundException {
        Recycle dbRecycle = recycleRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("inspectRequest does not exist. input id: " + id));
        dbRecycle.delete();
        recycleRepository.save(dbRecycle);
    }
}
