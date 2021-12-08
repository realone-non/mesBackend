package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ContractItemRequest;
import com.mes.mesBackend.dto.request.ContractRequest;
import com.mes.mesBackend.dto.response.ContractItemFileResponse;
import com.mes.mesBackend.dto.response.ContractItemResponse;
import com.mes.mesBackend.dto.response.ContractResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
// 4-2. 수주 등록
public interface ContractService {
    // ======================================== 수주 ===============================================
    // 수주 생성
    ContractResponse createContract(ContractRequest contractRequest) throws NotFoundException;
    // 수주 단일 조회
    ContractResponse getContract(Long contractId) throws NotFoundException;
    // 수주 리스트 조회
    List<ContractResponse> getContracts(String clientName, String userName, LocalDate fromDate, LocalDate toDate, Long currencyId);
    // 수주 수정
    ContractResponse updateContract(Long contractId, ContractRequest contractRequest) throws NotFoundException;
    // 수주 삭제
    void deleteContract(Long id) throws NotFoundException;

    // ======================================== 수주 품목 ===============================================
    // 수주 품목 생성
    ContractItemResponse createContractItem(Long contractId, ContractItemRequest contractItemRequest) throws NotFoundException;
    // 수주 품목 단일 조회
    ContractItemResponse getContractItem(Long contractId, Long contractItemId) throws NotFoundException;
    // 수주 품목 전체 조회
    List<ContractItemResponse> getContractItems(Long contractId) throws NotFoundException;
    // 수주 품목 수정
    ContractItemResponse updateContractItem(Long contractId, Long contractItemId, ContractItemRequest contractItemRequest) throws NotFoundException;
    // 수주 품목 삭제
    void deleteContractItem(Long contractId, Long contractItemId) throws NotFoundException;

    // ======================================== 수주 품목 파일 ===============================================
    // 수주 품목 파일 추가
    ContractItemFileResponse createBusinessFileToContractItemFile(Long contractId, Long contractItemId, MultipartFile file) throws NotFoundException, BadRequestException, IOException;
    // 수주 품목 파일 전체 조회
    List<ContractItemFileResponse> getItemFiles(Long contractId, Long contractItemId) throws NotFoundException;
    // 수주 품목 파일 삭제
    void deleteItemFile(Long contractId, Long contractItemId, Long contractItemFileId) throws NotFoundException;
}
