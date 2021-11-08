package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.FactoryRequest;
import com.mes.mesBackend.dto.response.FactoryResponse;
import com.mes.mesBackend.entity.Factory;
import com.mes.mesBackend.entity.WorkPlace;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.FactoryRepository;
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
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Factory getFactoryOrThrow(Long id) throws NotFoundException {
        return factoryRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("factory does not exists. input id: " + id));
    }

    // 공장 생성
    public FactoryResponse createFactory(FactoryRequest factoryRequest) throws NotFoundException {
        WorkPlace workPlace = workPlaceService.getWorkPlaceOrThrow(factoryRequest.getWorkPlaceId());
        Factory factory = modelMapper.toEntity(factoryRequest, Factory.class);
        factory.addJoin(workPlace);
        factoryRepository.save(factory);
        return modelMapper.toResponse(factory, FactoryResponse.class);
    }

    // 공장 조회
    public FactoryResponse getFactory(Long id) throws NotFoundException {
        Factory factory = getFactoryOrThrow(id);
        return modelMapper.toResponse(factory, FactoryResponse.class);
    }

    // 공장 전체 조회
    public Page<FactoryResponse> getFactories(Pageable pageable) {
        Page<Factory> factories = factoryRepository.findAllByDeleteYnFalse(pageable);
        return modelMapper.toPageResponses(factories, FactoryResponse.class);
    }

    // 공장 수정
    public FactoryResponse updateFactory(Long id, FactoryRequest factoryRequest) throws NotFoundException {
        WorkPlace newWorkPlace = workPlaceService.getWorkPlaceOrThrow(factoryRequest.getWorkPlaceId());
        Factory newFactory = modelMapper.toEntity(factoryRequest, Factory.class);
        Factory findFactory = getFactoryOrThrow(id);
        findFactory.put(newFactory, newWorkPlace);
        factoryRepository.save(findFactory);
        return modelMapper.toResponse(findFactory, FactoryResponse.class);
    }

    // 공장 삭제
    public void deleteFactory(Long id) throws NotFoundException {
        Factory factory = getFactoryOrThrow(id);
        factory.delete();
        factoryRepository.save(factory);
    }
}
