package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ClientRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.entity.Client;
import com.mes.mesBackend.entity.ClientType;
import com.mes.mesBackend.entity.CountryCode;
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
    private ClientRepository clientRepository;

    @Autowired
    private BusinessTypeService businessTypeService;

    @Autowired
    private ClientTypeService clientTypeService;

    @Autowired
    private CountryCodeService countryCodeService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    ModelMapper modelMapper;

    public Client findClientByIdAndDeleteYnFalse(Long id) throws NotFoundException {
        Client findClient = clientRepository.findByIdAndDeleteYnFalse(id);
        if (findClient == null) {
            throw new NotFoundException("client does not exist. input client id: " + id);
        }
        return findClient;
    }

    // 거래처 생성
    public ClientResponse createClient(ClientRequest clientRequest) throws NotFoundException {
        Long businessTypeId = clientRequest.getBusinessType();
        Long countryCodeId = clientRequest.getCountryCode();
        Long clientTypeId = clientRequest.getClientType();

        BusinessType businessType = businessTypeService.findBusinessTypeByIdAndDeleteYn(businessTypeId);
        CountryCode countryCode = countryCodeService.findCountryCodeByIdAndDeleteYn(countryCodeId);
        ClientType clientType = clientTypeService.findClientTypeByIdAndDeleteYn(clientTypeId);

        Client client = modelMapper.toEntity(clientRequest, Client.class);

        client.putJoinTable(businessType, countryCode, clientType);
        Client saveClient = clientRepository.save(client);

        return modelMapper.toResponse(saveClient, ClientResponse.class);
    }

    // 거래처 조회
    public ClientResponse getClient(Long id) throws NotFoundException {
        Client client = findClientByIdAndDeleteYnFalse(id);
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
        Client findClient = findClientByIdAndDeleteYnFalse(id);

        BusinessType findBusinessType = businessTypeService.findBusinessTypeByIdAndDeleteYn(clientRequest.getBusinessType());
        ClientType findClientType = clientTypeService.findClientTypeByIdAndDeleteYn(clientRequest.getClientType());
        CountryCode findCountryCode = countryCodeService.findCountryCodeByIdAndDeleteYn(clientRequest.getCountryCode());

        newClient.putJoinTable(findBusinessType, findCountryCode, findClientType);
        findClient.put(newClient);

        Client updateClient = clientRepository.save(findClient);
        return modelMapper.toResponse(updateClient, ClientResponse.class);
    }

    // 사업자 등록증 파일 업로드
    // client/거래처 명/파일명(날싸시간)
    public ClientResponse createBusinessFileToClient(Long id, MultipartFile businessFile) throws IOException, NotFoundException {
        Client client = findClientByIdAndDeleteYnFalse(id);
        String fileName = "client/" + client.getClientCode() + "/";
        client.setBusinessFile(s3Service.upload(businessFile, fileName));
        clientRepository.save(client);
        return modelMapper.toResponse(client, ClientResponse.class);
    }

    // 거래처 삭제
    public void deleteClient(Long id) throws NotFoundException {
        Client client = findClientByIdAndDeleteYnFalse(id);
        client.setDeleteYn(true);
        clientRepository.save(client);
    }

    // 사업자 등록증 파일 삭제 (aws 권한 문제로 안됨)
    private void deleteBusinessFileToClient(Long id) throws IOException, NotFoundException {
        Client client = findClientByIdAndDeleteYnFalse(id);
        s3Service.delete(client.getBusinessFile());
        client.setBusinessFile(null);
    }
}
