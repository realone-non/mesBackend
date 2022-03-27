package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ClientTypeRequest;
import com.mes.mesBackend.dto.response.ClientTypeResponse;
import com.mes.mesBackend.entity.ClientType;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ClientTypeRepository;
import com.mes.mesBackend.service.ClientTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientTypeServiceImpl implements ClientTypeService {

    @Autowired
    private ClientTypeRepository clientTypeRepository;

    @Autowired
    ModelMapper modelMapper;

    public ClientType getClientTypeOrThrow(Long id) throws NotFoundException {
        return clientTypeRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("client type does not exist. input clientTypeId: " + id));
    }

    // 거래처유형 생성
    public ClientTypeResponse createClientType(ClientTypeRequest clientTypeRequest) {
        ClientType clientType = modelMapper.toEntity(clientTypeRequest, ClientType.class);
        clientTypeRepository.save(clientType);
        return modelMapper.toResponse(clientType, ClientTypeResponse.class);
    }

    // 거래처유형 조회
    public ClientTypeResponse getClientType(Long id) throws NotFoundException {
        ClientType clientType = getClientTypeOrThrow(id);
        return modelMapper.toResponse(clientType, ClientTypeResponse.class);
    }

    // 거래처유형 전체 조회
    public List<ClientTypeResponse> getClientTypes() {
        List<ClientType> clientTypes = clientTypeRepository.findAllByDeleteYnFalseOrderByCreatedDateDesc();
        return modelMapper.toListResponses(clientTypes, ClientTypeResponse.class);
    }
//    public Page<ClientTypeResponse> getClientTypes(Pageable pageable) {
//        Page<ClientType> clientTypes = clientTypeRepository.findAllByDeleteYnFalse(pageable);
//        return modelMapper.toPageResponses(clientTypes, ClientTypeResponse.class);
//    }

    // 거래처유형 수정
    public ClientTypeResponse updateClientType(Long id, ClientTypeRequest clientTypeRequest) throws NotFoundException {
        ClientType newClientType = modelMapper.toEntity(clientTypeRequest, ClientType.class);
        ClientType findClientType = getClientTypeOrThrow(id);
        findClientType.put(newClientType);
        clientTypeRepository.save(findClientType);
        return modelMapper.toResponse(findClientType, ClientTypeResponse.class);
    }

    // 거래처유형 삭제
    public void deleteClientType(Long id) throws NotFoundException {
        ClientType clientType = getClientTypeOrThrow(id);
        clientType.delete();
        clientTypeRepository.save(clientType);
    }
}
