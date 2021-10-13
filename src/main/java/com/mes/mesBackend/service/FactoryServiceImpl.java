package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.FactoryRequest;
import com.mes.mesBackend.dto.response.FactoryResponse;
import com.mes.mesBackend.entity.Factory;
import com.mes.mesBackend.entity.WorkPlace;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.FactoryRepository;
import com.mes.mesBackend.repository.WorkPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired FactoryRepository factoryRepository;
    @Autowired WorkPlaceRepository workPlaceRepository;
    @Autowired Mapper mapper;

    // 공장 생성
    public FactoryResponse createFactory(FactoryRequest factoryRequest) {
        WorkPlace workPlace = workPlaceRepository.findByIdAndDeleteYnFalse(factoryRequest.getWorkPlaceId());
        Factory factory = mapper.toEntity(factoryRequest, Factory.class);
        factory.setWorkPlace(workPlace);
        Factory saveFactory = factoryRepository.save(factory);
        return mapper.toResponse(saveFactory, FactoryResponse.class);
    }

    // 공장 조회
    public FactoryResponse getFactory(Long id) {
        Factory factory = factoryRepository.findByIdAndDeleteYnFalse(id);
        return mapper.toResponse(factory, FactoryResponse.class);
    }

    // 공장 전체 조회
    public Page<FactoryResponse> getFactories(Pageable pageable) {
        Page<Factory> factories = factoryRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(factories, FactoryResponse.class);
    }

    // 공장 수정
    public FactoryResponse updateFactory(Long id, FactoryRequest factoryRequest) {
        Factory newFactory = mapper.toEntity(factoryRequest, Factory.class);
        WorkPlace workPlace = workPlaceRepository.findByIdAndDeleteYnFalse(factoryRequest.getWorkPlaceId());
        Factory findFactory = factoryRepository.findByIdAndDeleteYnFalse(id);
        findFactory.put(newFactory, workPlace);
        Factory updateFactory = factoryRepository.save(findFactory);
        return mapper.toResponse(updateFactory, FactoryResponse.class);
    }

    // 공장 삭제
    public void deleteFactory(Long id) {
        Factory factory = factoryRepository.findByIdAndDeleteYnFalse(id);
        factory.setDeleteYn(true);
        factoryRepository.save(factory);
    }
}
