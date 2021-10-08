package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ClientRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.entity.Client;
import com.mes.mesBackend.entity.ClientType;
import com.mes.mesBackend.entity.CountryCode;
import com.mes.mesBackend.helper.S3Service;
import com.mes.mesBackend.repository.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    public Client findClientByIdAndDeleteYnTrue(Long id) {
        return clientRepository.findByIdAndDeleteYnFalse(id);
    }

    // 거래처 생성
    public ClientResponse createClient(ClientRequest clientRequest) {
        Long businessTypeId = clientRequest.getBusinessTypeId();
        Long countryCodeId = clientRequest.getCountryCodeId();
        Long clientTypeId = clientRequest.getClientType();

        BusinessType businessType = businessTypeService.findBusinessTypeByIdAndDeleteYn(businessTypeId);
        CountryCode countryCode = countryCodeService.findCountryCodeByIdAndDeleteYn(countryCodeId);
        ClientType clientType = clientTypeService.findClientTypeByIdAndDeleteYn(clientTypeId);

        Client client = clientRequestToClient(clientRequest);

//        client.setBusinessType(businessType);
//        client.setCountryCode(countryCode);
//        client.setClientType(clientType);

        client.putJoinTable(businessType, countryCode, clientType);
        Client saveClient = clientRepository.save(client);

        return clientToClientResponse(saveClient);
    }

    // 거래처 조회
    public ClientResponse getClient(Long id) {
        Client client = findClientByIdAndDeleteYnTrue(id);
        return clientToClientResponse(client);
    }

    // 거래처 조건 페이징 조회 (거래처 유형, 거래처 코드, 거래처 명)
    public Page<ClientResponse> getClients(
            Long type,
            String code,
            String clientName,
            Pageable pageable
    ) {
        Page<Client> clients = clientRepository.findByTypeAndCodeAndName(type, code, clientName, pageable);
        return clientToPageClientResponses(clients);
    }

    // 거래처 수정
    public ClientResponse updateClient(Long id, ClientRequest clientRequest) {
        Client newClient = clientRequestToClient(clientRequest);
        Client findClient = findClientByIdAndDeleteYnTrue(id);

        BusinessType findBusinessType = businessTypeService.findBusinessTypeByIdAndDeleteYn(clientRequest.getBusinessTypeId());
        ClientType findClientType = clientTypeService.findClientTypeByIdAndDeleteYn(clientRequest.getClientType());
        CountryCode findCountryCode = countryCodeService.findCountryCodeByIdAndDeleteYn(clientRequest.getCountryCodeId());

        newClient.putJoinTable(findBusinessType, findCountryCode, findClientType);
        findClient.put(newClient);

        Client updateClient = clientRepository.save(findClient);
        return clientToClientResponse(updateClient);
    }

    // 사업자 등록증 파일 업로드
    // client/거래처 명/파일명(날싸시간)
    public ClientResponse createBusinessFileToClient(Long id, MultipartFile businessFile) throws IOException {
        Client client = findClientByIdAndDeleteYnTrue(id);
        String fileName = "client/" + client.getClientCode() + "/";
        client.setBusinessFile(s3Service.upload(businessFile, fileName));
        clientRepository.save(client);
        return clientToClientResponse(client);
    }

    // 거래처 삭제
    public void deleteClient(Long id) {
        Client client = findClientByIdAndDeleteYnTrue(id);
        client.setDeleteYn(true);
        clientRepository.save(client);
    }

    // Entity -> Response
    private ClientResponse clientToClientResponse(Client client) {
        return modelMapper.map(client, ClientResponse.class);
    }

    // List<entity> -> List<Response>
    private List<ClientResponse> clientToListClientResponses(List<Client> clients) {
        return clients
                .stream()
                .map(client ->
                        modelMapper.map(client, ClientResponse.class)).collect(Collectors.toList());
    }

    // Request -> Entity
    private Client clientRequestToClient(ClientRequest clientRequest) {
        return modelMapper.map(clientRequest, Client.class);
    }

    // Page<Entity> -> Page<Response>
    private Page<ClientResponse> clientToPageClientResponses(Page<Client> clients) {
        return clients.map(client -> modelMapper.map(client, ClientResponse.class));
    }

    // 사업자 등록증 파일 삭제 (aws 권한 문제로 안됨)
    private void deleteBusinessFileToClient(Long id) throws IOException {
        Client client = findClientByIdAndDeleteYnTrue(id);
        s3Service.delete(client.getBusinessFile());
        client.setBusinessFile(null);
    }
}
