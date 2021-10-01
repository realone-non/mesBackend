package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ClientRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ClientService {

    // 거래처 생성
    ClientResponse createClient(ClientRequest clientRequest);

    // 거래처 조회
    ClientResponse getClient(Long id);

    // 거래처 리스트 조회
    Page<ClientResponse> getClients(Pageable pageable);

    // 거래처 수정
    ClientResponse updateClient(Long id, ClientRequest clientRequest);

    // 거래처 삭제
    void deleteClient(Long id);

    // 사업자 등록증 파일 업로드
    ClientResponse createBusinessFileToClient(Long id, MultipartFile businessFile) throws IOException;

    Client findClientByIdAndUseYnTrue(Long id);
}
