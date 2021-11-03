package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ClientTypeRequest;
import com.mes.mesBackend.dto.response.ClientTypeResponse;
import com.mes.mesBackend.entity.ClientType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
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
    ModelMapper modelMapper;

    public ClientType getClientTypeOrThrow(Long id) throws NotFoundException {
        ClientType findClientType = clientTypeRepository.findByIdAndDeleteYnFalse(id);
        if (findClientType == null) {
            throw new NotFoundException("client type does not exist. input clientTypeId: " + id);
        }
        return findClientType;
    }

    // 거래처유형 생성
    public ClientTypeResponse createClientType(ClientTypeRequest clientTypeRequest) {
        ClientType clientType = modelMapper.toEntity(clientTypeRequest, ClientType.class);
        ClientType saveClientType = clientTypeRepository.save(clientType);
        return modelMapper.toResponse(saveClientType, ClientTypeResponse.class);
    }

    // 거래처유형 조회
    public ClientTypeResponse getClientType(Long id) throws NotFoundException {
        ClientType clientType = getClientTypeOrThrow(id);
        return modelMapper.toResponse(clientType, ClientTypeResponse.class);
    }

    // 거래처유형 전체 조회
    public Page<ClientTypeResponse> getClientTypes(Pageable pageable) {
        Page<ClientType> clientTypes = clientTypeRepository.findAllByDeleteYnFalse(pageable);
        return modelMapper.toPageResponses(clientTypes, ClientTypeResponse.class);
    }

    // 거래처유형 수정
    public ClientTypeResponse updateClientType(Long id, ClientTypeRequest clientTypeRequest) throws NotFoundException {
        ClientType clientType = modelMapper.toEntity(clientTypeRequest, ClientType.class);
        ClientType findClientType = getClientTypeOrThrow(id);
        findClientType.setName(clientType.getName());
        ClientType updateClientType = clientTypeRepository.save(findClientType);
        return modelMapper.toResponse(updateClientType, ClientTypeResponse.class);
    }

    // 거래처유형 삭제
    public void deleteClientType(Long id) throws NotFoundException {
        ClientType clientType = getClientTypeOrThrow(id);
        clientType.setDeleteYn(true);
        clientTypeRepository.save(clientType);
    }
}
