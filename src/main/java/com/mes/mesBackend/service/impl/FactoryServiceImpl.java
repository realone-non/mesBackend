package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.FactoryRequest;
import com.mes.mesBackend.dto.response.FactoryResponse;
import com.mes.mesBackend.entity.Factory;
import com.mes.mesBackend.entity.WorkPlace;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.FactoryRepository;
import com.mes.mesBackend.repository.WorkPlaceRepository;
import com.mes.mesBackend.service.FactoryService;
import com.mes.mesBackend.service.WorkPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired FactoryRepository factoryRepository;
    @Autowired WorkPlaceService workPlaceService;
    @Autowired Mapper mapper;

    @Override
    public Factory findByIdAndDeleteYnFalse(Long id) throws NotFoundException {
        Factory findFactory = factoryRepository.findByIdAndDeleteYnFalse(id);
        if (findFactory == null) throw new NotFoundException("factory does not exists. input id: " + id);
        return findFactory;
    }

    // 공장 생성
    public FactoryResponse createFactory(FactoryRequest factoryRequest) throws NotFoundException {
        WorkPlace workPlace = workPlaceService.findByIdAndDeleteYnFalse(factoryRequest.getWorkPlaceId());
        Factory factory = mapper.toEntity(factoryRequest, Factory.class);
        factory.setWorkPlace(workPlace);
        Factory saveFactory = factoryRepository.save(factory);
        return mapper.toResponse(saveFactory, FactoryResponse.class);
    }

    // 공장 조회
    public FactoryResponse getFactory(Long id) throws NotFoundException {
        Factory factory = findByIdAndDeleteYnFalse(id);
        return mapper.toResponse(factory, FactoryResponse.class);
    }

    // 공장 전체 조회
    public Page<FactoryResponse> getFactories(Pageable pageable) {
        Page<Factory> factories = factoryRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(factories, FactoryResponse.class);
    }

    // 공장 수정
    public FactoryResponse updateFactory(Long id, FactoryRequest factoryRequest) throws NotFoundException {
        Factory newFactory = mapper.toEntity(factoryRequest, Factory.class);
        WorkPlace workPlace = workPlaceService.findByIdAndDeleteYnFalse(factoryRequest.getWorkPlaceId());
        Factory findFactory = findByIdAndDeleteYnFalse(id);
        findFactory.put(newFactory, workPlace);
        Factory updateFactory = factoryRepository.save(findFactory);
        return mapper.toResponse(updateFactory, FactoryResponse.class);
    }

    // 공장 삭제
    public void deleteFactory(Long id) throws NotFoundException {
        Factory factory = findByIdAndDeleteYnFalse(id);
        factory.setDeleteYn(true);
        factoryRepository.save(factory);
    }
}
