package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ClientRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.entity.Client;
import com.mes.mesBackend.entity.ClientType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ClientService {

    // 거래처 생성
    ClientResponse createClient(ClientRequest clientRequest) throws NotFoundException;

    // 거래처 조회
    ClientResponse getClient(Long id) throws NotFoundException;

    // 거래처 조건 페이징 조회 (거래처 유형, 거래처 코드, 거래처 명)
    List<ClientResponse> getClients(Long type, String clientCode, String name);

    // 거래처 수정
    ClientResponse updateClient(Long id, ClientRequest clientRequest) throws NotFoundException;

    // 거래처 삭제
    void deleteClient(Long id) throws NotFoundException;

    // 사업자 등록증 파일 업로드
    ClientResponse createBusinessFileToClient(Long id, MultipartFile businessFile) throws IOException, NotFoundException, BadRequestException;

    Client getClientOrThrow(Long id) throws NotFoundException;
}
