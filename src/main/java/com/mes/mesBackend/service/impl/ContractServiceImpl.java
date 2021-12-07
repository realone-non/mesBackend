package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ContractItemRequest;
import com.mes.mesBackend.dto.request.ContractRequest;
import com.mes.mesBackend.dto.response.ContractItemFileResponse;
import com.mes.mesBackend.dto.response.ContractItemResponse;
import com.mes.mesBackend.dto.response.ContractResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ContractRepository;
import com.mes.mesBackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.DATE_TIME_FORMAT;

// 4-2. 수주 등록
@Service
public class ContractServiceImpl implements ContractService {
    @Autowired
    ContractRepository contractRepo;
    @Autowired
    ModelMapper mapper;
    @Autowired
    ClientService clientService;
    @Autowired
    UserService userService;
    @Autowired
    CurrencyService currencyService;
    @Autowired
    WareHouseService wareHouseService;

    // ======================================== 수주 ===============================================
    // 수주 생성
    @Override
    public ContractResponse createContract(ContractRequest contractRequest) throws NotFoundException {
        Client client = clientService.getClientOrThrow(contractRequest.getClient());
        User user = userService.getUserOrThrow(contractRequest.getUser());
        Currency currency = currencyService.getCurrencyOrThrow(contractRequest.getCurrency());
        WareHouse outPutWareHouse = wareHouseService.getWareHouseOrThrow(contractRequest.getOutputWareHouse());
        Contract contract = mapper.toEntity(contractRequest, Contract.class);
        contract.addJoin(client, user, currency, outPutWareHouse);
        contract.setContractNo(createContractNo());
        contractRepo.save(contract);
        return mapper.toResponse(contract, ContractResponse.class);
    }

    // 수주 단일 조회
    @Override
    public ContractResponse getContract(Long contractId) throws NotFoundException {
        Contract contract = getContractOrThrow(contractId);
        return mapper.toResponse(contract, ContractResponse.class);
    }

    // 수주 리스트 조회
    @Override
    public List<ContractResponse> getContracts(String clientName, String userName, LocalDate fromDate, LocalDate toDate, Long currencyId) {
        List<Contract> contracts = contractRepo.findAllByCondition(clientName, userName, fromDate, toDate, currencyId);
        return mapper.toListResponses(contracts, ContractResponse.class);
    }

    // 수주 수정
    @Override
    public ContractResponse updateContract(Long contractId, ContractRequest contractRequest) throws NotFoundException {
        Client newClient = clientService.getClientOrThrow(contractRequest.getClient());
        User newUser = userService.getUserOrThrow(contractRequest.getUser());
        Currency newCurrency = currencyService.getCurrencyOrThrow(contractRequest.getCurrency());
        WareHouse newOutPutWareHouse = wareHouseService.getWareHouseOrThrow(contractRequest.getOutputWareHouse());
        Contract newContract = mapper.toEntity(contractRequest, Contract.class);
        Contract findContract = getContractOrThrow(contractId);
        findContract.update(newContract, newClient, newUser, newCurrency, newOutPutWareHouse);
        contractRepo.save(findContract);
        return mapper.toResponse(findContract, ContractResponse.class);
    }

    // 수주 삭제
    @Override
    public void deleteContract(Long id) {

    }

    // 수주 단일 조회 및 예외
    private Contract getContractOrThrow(Long id) throws NotFoundException {
        return contractRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("contract does not exist. input id: " + id));
    }

    // 수주 번호 생성
    private String createContractNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    // ======================================== 수주 품목 ===============================================

    // 수주 품목 생성
    @Override
    public ContractItemResponse createContractItem(ContractItemRequest contractItemRequest) {
//        getContr
        return null;
    }

    // 수주 품목 단일 조회
    @Override
    public ContractItemResponse getContractItem(Long contractId, Long contractItemId) {
        return null;
    }

    // 수주 품목 전체 조회
    @Override
    public List<ContractItemResponse> getContractItems(Long contractId) {
        return null;
    }

    // 수주 품목 수정
    @Override
    public ContractItemResponse updateContractItem(Long contractId, Long contractItemId, ContractItemRequest contractItemRequest) {
        return null;
    }

    // 수주 품목 삭제
    @Override
    public void deleteContractItem(Long contractId, Long contractItemId) {

    }
    // ======================================== 수주 품목 파일 ===============================================
    // 수주 품목 파일 추가
    @Override
    public ContractItemFileResponse createBusinessFileToContractItemFile(Long contractId, Long contractItemId, MultipartFile file) {
        return null;
    }
    // 수주 품목 파일 전체 조회
    @Override
    public List<ContractItemFileResponse> getItemFiles(Long contractId, Long contractItemId) {
        return null;
    }
    // 수주 품목 파일 삭제
    @Override
    public void deleteItemFile(Long contractId, Long contractItemId, Long contractItemFileId) {

    }
}
