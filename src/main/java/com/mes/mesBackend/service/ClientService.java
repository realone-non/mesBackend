package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ClientRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ClientService {

    // 거래처 생성
    ClientResponse createClient(ClientRequest clientRequest);

    // 거래처 조회
    ClientResponse getClient(Long id);

    // 거래처 리스트 조회
    List<ClientResponse> getClients();

    // 거래처 수정
    ClientResponse updateClient(Long id, ClientRequest clientRequest);

    // 거래처 삭제
    void deleteClient(Long id);

    // 사업자 등록증 파일 업로드
    ClientResponse createBusinessFileToClient(Long id, MultipartFile businessFile) throws IOException;
}
