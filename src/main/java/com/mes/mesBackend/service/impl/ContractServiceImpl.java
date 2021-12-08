package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ContractItemRequest;
import com.mes.mesBackend.dto.request.ContractRequest;
import com.mes.mesBackend.dto.response.ContractItemFileResponse;
import com.mes.mesBackend.dto.response.ContractItemResponse;
import com.mes.mesBackend.dto.response.ContractResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.S3Uploader;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ContractItemFileRepository;
import com.mes.mesBackend.repository.ContractItemRepository;
import com.mes.mesBackend.repository.ContractRepository;
import com.mes.mesBackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired
    ContractItemRepository contractItemRepo;
    @Autowired
    ItemService itemService;
    @Autowired
    S3Uploader s3Uploader;
    @Autowired
    ContractItemFileRepository contractItemFileRepo;
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
    public void deleteContract(Long id) throws NotFoundException {
        Contract contract = getContractOrThrow(id);
        contract.delete();
        contractRepo.save(contract);
        List<ContractItem> contractItems = contractItemRepo.findAllByContractAndDeleteYnFalse(contract);
        // 해당 수주에 해당하는 수주품목, 수주품목파일 같이 삭제
        for (ContractItem contractItem : contractItems) {
            // 수주품목 삭제
            contractItem.delete();
            contractItemRepo.save(contractItem);
            // 수주품목 파일 삭제
            List<ContractItemFile> itemFiles = contractItemFileRepo.findAllByContractItemAndDeleteYnFalse(contractItem);
            itemFiles.forEach(ContractItemFile::delete);
            contractItemFileRepo.saveAll(itemFiles);
        }
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
    public ContractItemResponse createContractItem(Long contractId, ContractItemRequest contractItemRequest) throws NotFoundException {
        Contract contract = getContractOrThrow(contractId);
        Item item = itemService.getItemOrThrow(contractItemRequest.getItem());
        ContractItem contractItem = mapper.toEntity(contractItemRequest, ContractItem.class);
        contractItem.addJoin(contract, item);
        contractItemRepo.save(contractItem);
        return mapper.toResponse(contractItem, ContractItemResponse.class);
    }

    // 수주 품목 단일 조회
    @Override
    public ContractItemResponse getContractItem(Long contractId, Long contractItemId) throws NotFoundException {
        Contract contract = getContractOrThrow(contractId);
        ContractItem contractItem = getContractItemOrThrow(contractId, contract);
        return mapper.toResponse(contractItem, ContractItemResponse.class);
    }

    // 수주 품목 전체 조회
    @Override
    public List<ContractItemResponse> getContractItems(Long contractId) throws NotFoundException {
        Contract contract = getContractOrThrow(contractId);
        List<ContractItem> contractItems = contractItemRepo.findAllByContractAndDeleteYnFalse(contract);
        return mapper.toListResponses(contractItems, ContractItemResponse.class);
    }

    // 수주 품목 수정
    @Override
    public ContractItemResponse updateContractItem(Long contractId, Long contractItemId, ContractItemRequest contractItemRequest) throws NotFoundException {
        Contract contract = getContractOrThrow(contractId);
        ContractItem findContractItem = getContractItemOrThrow(contractItemId, contract);
        Item newItem = itemService.getItemOrThrow(contractItemRequest.getItem());
        ContractItem newContractItem = mapper.toEntity(contractItemRequest, ContractItem.class);
        findContractItem.update(newContractItem, newItem);
        contractItemRepo.save(findContractItem);
        return mapper.toResponse(findContractItem, ContractItemResponse.class);
    }

    // 수주 품목 삭제
    @Override
    public void deleteContractItem(Long contractId, Long contractItemId) throws NotFoundException {
        Contract contract = getContractOrThrow(contractId);
        ContractItem contractItem = getContractItemOrThrow(contractItemId, contract);
        contractItem.delete();
        contractItemRepo.save(contractItem);
    }

    // 수주 품목 단일 조회 및 예외
    private ContractItem getContractItemOrThrow(Long id, Contract contract) throws NotFoundException {
        return contractItemRepo.findByIdAndContractAndDeleteYnFalse(id, contract)
                .orElseThrow(() -> new NotFoundException("contract item does not exist. input id: " + id));
    }

    // ======================================== 수주 품목 파일 ===============================================
    // 수주 품목 파일 추가
    // 파일명: contract-items/수주번호/고객발주번호/파일명(현재날짜)
    @Override
    public ContractItemFileResponse createBusinessFileToContractItemFile(Long contractId, Long contractItemId, MultipartFile file) throws NotFoundException, BadRequestException, IOException {
        Contract contract = getContractOrThrow(contractId);
        ContractItem contractItem = getContractItemOrThrow(contractItemId, contract);

        String fileName = "contract-items/" + contract.getContractNo() + "/" + contract.getClientOrderNo() + "/";
        ContractItemFile contractItemFile = new ContractItemFile();

        String uploadFileUrl = s3Uploader.upload(file, fileName);
        contractItemFile.add(contractItem, uploadFileUrl);

        contractItemFileRepo.save(contractItemFile);
        return mapper.toResponse(contractItemFile, ContractItemFileResponse.class);
    }

    // 수주 품목 파일 전체 조회
    @Override
    public List<ContractItemFileResponse> getItemFiles(Long contractId, Long contractItemId) throws NotFoundException {
        Contract contract = getContractOrThrow(contractId);
        ContractItem contractItem = getContractItemOrThrow(contractItemId, contract);
        List<ContractItemFile> contractItemFiles = contractItemFileRepo.findAllByContractItemAndDeleteYnFalse(contractItem);
        return mapper.toListResponses(contractItemFiles, ContractItemFileResponse.class);
    }

    // 수주 품목 파일 삭제
    @Override
    public void deleteItemFile(Long contractId, Long contractItemId, Long contractItemFileId) throws NotFoundException {
        Contract contract = getContractOrThrow(contractId);
        ContractItem contractItem = getContractItemOrThrow(contractItemId, contract);
        ContractItemFile contractItemFile = contractItemFileRepo.findByIdAndContractItemAndDeleteYnFalse(contractItemFileId, contractItem);
        contractItemFile.delete();
        contractItemFileRepo.save(contractItemFile);
    }

    // ContractItem 의 file 갯수
//    private String getContractItemFileCnt(Long contractId, Long contractItemId) throws NotFoundException {
//        Contract contract = getContractOrThrow(contractId);
//        ContractItem contractItem = getContractItemOrThrow(contractItemId, contract);
//        int fileCnt = contractItemFileRepo.countAllByContractItemAndDeleteYnFalse(contractItem);
//        return fileCnt + " Files";
//    }
}
