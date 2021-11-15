package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ClientRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.entity.Client;
import com.mes.mesBackend.entity.ClientType;
import com.mes.mesBackend.entity.CountryCode;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.helper.S3Service;
import com.mes.mesBackend.repository.ClientRepository;
import com.mes.mesBackend.service.BusinessTypeService;
import com.mes.mesBackend.service.ClientService;
import com.mes.mesBackend.service.ClientTypeService;
import com.mes.mesBackend.service.CountryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    BusinessTypeService businessTypeService;

    @Autowired
    ClientTypeService clientTypeService;

    @Autowired
    CountryCodeService countryCodeService;

    @Autowired
    S3Service s3Service;

    @Autowired
    ModelMapper modelMapper;

    public Client getClientOrThrow(Long id) throws NotFoundException {
        return clientRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("client does not exist. input client id: " + id));
    }

    // 거래처 생성
    public ClientResponse createClient(ClientRequest clientRequest) throws NotFoundException {
        BusinessType businessType = clientRequest.getBusinessType() != null ? businessTypeService.getBusinessTypeOrThrow(clientRequest.getBusinessType()) : null;
        CountryCode countryCode = clientRequest.getCountryCode() != null ? countryCodeService.getCountryCodeOrThrow(clientRequest.getCountryCode()) : null;
        ClientType clientType = clientTypeService.getClientTypeOrThrow(clientRequest.getClientType());

        Client client = modelMapper.toEntity(clientRequest, Client.class);
        client.addJoin(businessType, countryCode, clientType);
        return modelMapper.toResponse(client, ClientResponse.class);
    }

    // 거래처 조회
    public ClientResponse getClient(Long id) throws NotFoundException {
        Client client = getClientOrThrow(id);
        return modelMapper.toResponse(client, ClientResponse.class);
    }

    // 거래처 조건 페이징 조회 (거래처 유형, 거래처 코드, 거래처 명)
    public Page<ClientResponse> getClients(
            Long type,
            String code,
            String clientName,
            Pageable pageable
    ) {
        Page<Client> clients = clientRepository.findByTypeAndCodeAndName(type, code, clientName, pageable);
        return modelMapper.toPageResponses(clients, ClientResponse.class);
    }

    // 거래처 수정
    public ClientResponse updateClient(Long id, ClientRequest clientRequest) throws NotFoundException {
        Client newClient = modelMapper.toEntity(clientRequest, Client.class);
        Client findClient = getClientOrThrow(id);

        BusinessType newBusinessType = clientRequest.getBusinessType() != null ? businessTypeService.getBusinessTypeOrThrow(clientRequest.getBusinessType()) : null;
        CountryCode newCountryCode = clientRequest.getCountryCode() != null ? countryCodeService.getCountryCodeOrThrow(clientRequest.getCountryCode()) : null;
        ClientType newClientType = clientTypeService.getClientTypeOrThrow(clientRequest.getClientType());

        findClient.put(newClient, newBusinessType, newCountryCode, newClientType);
        clientRepository.save(findClient);
        return modelMapper.toResponse(findClient, ClientResponse.class);
    }

    // 사업자 등록증 파일 업로드
    // client/거래처 명/파일명(날싸시간)
    public ClientResponse createBusinessFileToClient(Long id, MultipartFile businessFile) throws IOException, NotFoundException, BadRequestException {
        Client client = getClientOrThrow(id);
        String fileName = "client/" + client.getClientCode() + "/";
        client.setBusinessFile(s3Service.upload(businessFile, fileName));
        clientRepository.save(client);
        return modelMapper.toResponse(client, ClientResponse.class);
    }

    // 거래처 삭제
    public void deleteClient(Long id) throws NotFoundException {
        Client client = getClientOrThrow(id);
        client.delete();
        clientRepository.save(client);
    }

    // 사업자 등록증 파일 삭제 (aws 권한 문제로 안됨)
    private void deleteBusinessFileToClient(Long id) throws IOException, NotFoundException {
        Client client = getClientOrThrow(id);
        s3Service.delete(client.getBusinessFile());
        client.setBusinessFile(null);
    }
}
