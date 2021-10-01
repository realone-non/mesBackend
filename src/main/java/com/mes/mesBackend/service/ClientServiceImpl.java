package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ClientRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.entity.Client;
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
    private CountryCodeService countryCodeService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    ModelMapper modelMapper;

    public Client findClientByIdAndUseYnTrue(Long id) {
        return clientRepository.findByIdAndUseYnTrue(id);
    }

    // 거래처 생성
    public ClientResponse createClient(ClientRequest clientRequest) {
        Long businessTypeId = clientRequest.getBusinessTypeId();
        Long countryCodeId = clientRequest.getCountryCodeId();

        BusinessType businessType = businessTypeService.findBusinessTypeByIdAndUseYn(businessTypeId);
        CountryCode countryCode = countryCodeService.findCountryCodeByIdAndUseYn(countryCodeId);

        Client client = clientRequestToClient(clientRequest);

        client.setBusinessType(businessType);
        client.setCountryCode(countryCode);

        Client saveClient = clientRepository.save(client);

        return clientToClientResponse(saveClient);
    }

    // 거래처 조회
    public ClientResponse getClient(Long id) {
        Client client = findClientByIdAndUseYnTrue(id);
        return clientToClientResponse(client);
    }

    // 거래처 리스트 조회
    public Page<ClientResponse> getClients(Pageable pageable) {
        Page<Client> clients = clientRepository.findAllByUseYnTrue(pageable);
        return clientToPageClientResponses(clients);
    }

    // 거래처 수정
    public ClientResponse updateClient(Long id, ClientRequest clientRequest) {
        Client client = clientRequestToClient(clientRequest);
        Client findClient = findClientByIdAndUseYnTrue(id);
        findClient.setName(client.getName());
        Client updateClient = clientRepository.save(findClient);
        return clientToClientResponse(updateClient);
    }

    // 사업자 등록증 파일 업로드
    // client/거래처 명/파일명(날싸시간)
    public ClientResponse createBusinessFileToClient(Long id, MultipartFile businessFile) throws IOException {
        Client client = findClientByIdAndUseYnTrue(id);
        String fileName = "client/" + client.getName() + "/";
        client.setBusinessFile(s3Service.upload(businessFile, fileName));
        clientRepository.save(client);
        return clientToClientResponse(client);
    }

    // 거래처 삭제
    public void deleteClient(Long id) {
        Client client = findClientByIdAndUseYnTrue(id);
        client.setUseYn(false);
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
        Client client = findClientByIdAndUseYnTrue(id);
        s3Service.delete(client.getBusinessFile());
        client.setBusinessFile(null);
    }
}
