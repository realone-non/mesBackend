package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ClientTypeRequest;
import com.mes.mesBackend.dto.response.ClientTypeResponse;
import com.mes.mesBackend.entity.ClientType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.Mapper;
import com.mes.mesBackend.repository.ClientTypeRepository;
import com.mes.mesBackend.service.ClientTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientTypeServiceImpl implements ClientTypeService {

    @Autowired
    private ClientTypeRepository clientTypeRepository;

    @Autowired
    Mapper mapper;

    public ClientType findClientTypeByIdAndDeleteYn(Long id) throws NotFoundException {
        ClientType findClientType = clientTypeRepository.findByIdAndDeleteYnFalse(id);
        if (findClientType == null) {
            throw new NotFoundException("client type does not exist. input clientTypeId: " + id);
        }
        return findClientType;
    }

    // 거래처유형 생성
    public ClientTypeResponse createClientType(ClientTypeRequest clientTypeRequest) {
        ClientType clientType = mapper.toEntity(clientTypeRequest, ClientType.class);
        ClientType saveClientType = clientTypeRepository.save(clientType);
        return mapper.toResponse(saveClientType, ClientTypeResponse.class);
    }

    // 거래처유형 조회
    public ClientTypeResponse getClientType(Long id) throws NotFoundException {
        ClientType clientType = findClientTypeByIdAndDeleteYn(id);
        return mapper.toResponse(clientType, ClientTypeResponse.class);
    }

    // 거래처유형 전체 조회
    public Page<ClientTypeResponse> getClientTypes(Pageable pageable) {
        Page<ClientType> clientTypes = clientTypeRepository.findAllByDeleteYnFalse(pageable);
        return mapper.toPageResponses(clientTypes, ClientTypeResponse.class);
    }

    // 거래처유형 수정
    public ClientTypeResponse updateClientType(Long id, ClientTypeRequest clientTypeRequest) throws NotFoundException {
        ClientType clientType = mapper.toEntity(clientTypeRequest, ClientType.class);
        ClientType findClientType = findClientTypeByIdAndDeleteYn(id);
        findClientType.setName(clientType.getName());
        ClientType updateClientType = clientTypeRepository.save(findClientType);
        return mapper.toResponse(updateClientType, ClientTypeResponse.class);
    }

    // 거래처유형 삭제
    public void deleteClientType(Long id) throws NotFoundException {
        ClientType clientType = findClientTypeByIdAndDeleteYn(id);
        clientType.setDeleteYn(true);
        clientTypeRepository.save(clientType);
    }
}
