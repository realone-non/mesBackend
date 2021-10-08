package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ClientTypeRequest;
import com.mes.mesBackend.dto.response.ClientTypeResponse;
import com.mes.mesBackend.entity.ClientType;
import com.mes.mesBackend.repository.ClientTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientTypeServiceImpl implements ClientTypeService {

    @Autowired
    private ClientTypeRepository clientTypeRepository;

    @Autowired
    ModelMapper modelMapper;

    public ClientType findClientTypeByIdAndDeleteYn(Long id) {
        return clientTypeRepository.findByIdAndDeleteYnFalse(id);
    }

    // 거래처유형 생성
    public ClientTypeResponse createClientType(ClientTypeRequest clientTypeRequest) {
        ClientType clientType = clientTypeRequestToClientType(clientTypeRequest);
        ClientType saveClientType = clientTypeRepository.save(clientType);
        return clientTypeToClientTypeResponse(saveClientType);
    }

    // 거래처유형 조회
    public ClientTypeResponse getClientType(Long id) {
        ClientType clientType = findClientTypeByIdAndDeleteYn(id);
        return clientTypeToClientTypeResponse(clientType);
    }

    // 거래처유형 전체 조회
    public Page<ClientTypeResponse> getClientTypes(Pageable pageable) {
        Page<ClientType> clientTypes = clientTypeRepository.findAllByDeleteYnFalse(pageable);
        return clientTypeToPageClientTypeResponses(clientTypes);
    }

    // 거래처유형 수정
    public ClientTypeResponse updateClientType(Long id, ClientTypeRequest clientTypeRequest) {
        ClientType clientType = clientTypeRequestToClientType(clientTypeRequest);
        ClientType findClientType = findClientTypeByIdAndDeleteYn(id);
        findClientType.setName(clientType.getName());
        ClientType updateClientType = clientTypeRepository.save(findClientType);
        return clientTypeToClientTypeResponse(updateClientType);
    }

    // 거래처유형 삭제
    public void deleteClientType(Long id) {
        ClientType clientType = findClientTypeByIdAndDeleteYn(id);
        clientType.setDeleteYn(true);
        clientTypeRepository.save(clientType);
    }

    // Entity -> Response
    private ClientTypeResponse clientTypeToClientTypeResponse(ClientType clientType) {
        return modelMapper.map(clientType, ClientTypeResponse.class);
    }

    // List<entity> -> List<Response>
    private List<ClientTypeResponse> clientTypeToListClientTypeResponse(List<ClientType> clientTypes) {
        return clientTypes
                .stream()
                .map(clientType ->
                        modelMapper.map(clientType, ClientTypeResponse.class)).collect(Collectors.toList());
    }

    // Request -> Entity
    private ClientType clientTypeRequestToClientType(ClientTypeRequest clientTypeRequest) {
        return modelMapper.map(clientTypeRequest, ClientType.class);
    }

    // Page<Entity> -> Page<Response>
    private Page<ClientTypeResponse> clientTypeToPageClientTypeResponses(Page<ClientType> clientTypes) {
        return clientTypes.map(clientType -> modelMapper.map(clientType, ClientTypeResponse.class));
    }

}
