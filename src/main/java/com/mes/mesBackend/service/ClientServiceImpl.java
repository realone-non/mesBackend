package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ClientRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.entity.Client;
import com.mes.mesBackend.repository.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private S3Service s3Service;

    @Autowired
    ModelMapper modelMapper;

    private Client findClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    // 거래처 생성
    public ClientResponse createClient(ClientRequest clientRequest) {
        Long businessTypeId = clientRequest.getBusinessTypeId();
        BusinessType businessType = businessTypeService.findBusinessType(businessTypeId);
        Client client = clientRequestToClient(clientRequest);
        client.setType(businessType);
        Client saveClient = clientRepository.save(client);
        return clientToClientResponse(saveClient);
    }

    // 거래처 조회
    public ClientResponse getClient(Long id) {
        Client client = findClientById(id);
        return clientToClientResponse(client);
    }

    // 거래처 리스트 조회
    public List<ClientResponse> getClients() {
        List<Client> clients = clientRepository.findAll();
        return clientToListClientResponses(clients);
    }

    // 거래처 수정
    public ClientResponse updateClient(Long id, ClientRequest clientRequest) {
        Client client = clientRequestToClient(clientRequest);
        Client findClient = findClientById(id);
        findClient.setName(client.getName());
        Client updateClient = clientRepository.save(findClient);
        return clientToClientResponse(updateClient);
    }

    // 사업자 등록증 파일 업로드
    public ClientResponse createBusinessFileToClient(Long id, MultipartFile businessFile) throws IOException {
        Client client = findClientById(id);
        client.setBusinessFile(s3Service.upload(businessFile, businessFile.getName()));
        return clientToClientResponse(client);
    }

    // 거래처 삭제
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
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
}
