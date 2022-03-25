package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ContractItemRequest;
import com.mes.mesBackend.dto.request.ContractRequest;
import com.mes.mesBackend.dto.response.ContractItemFileResponse;
import com.mes.mesBackend.dto.response.ContractItemResponse;
import com.mes.mesBackend.dto.response.ContractResponse;
import com.mes.mesBackend.entity.*;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.helper.NumberAutomatic;
import com.mes.mesBackend.helper.S3Uploader;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.*;
import com.mes.mesBackend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

// 4-2. 수주 등록
@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepo;
    private final ModelMapper mapper;
    private final ClientService clientService;
    private final UserService userService;
    private final CurrencyService currencyService;
    private final WareHouseService wareHouseService;
    private final ContractItemRepository contractItemRepo;
    private final ItemService itemService;
    private final S3Uploader s3Uploader;
    private final ContractItemFileRepository contractItemFileRepo;
    private final NumberAutomatic numberAutomatic;
    private final PayTypeRepository payTypeRepository;
    private final ProduceOrderRepository produceOrderRepository;
    // ======================================== 수주 ===============================================
    // 수주 생성
    @Override
    public ContractResponse createContract(ContractRequest contractRequest) throws NotFoundException {
        Client client = clientService.getClientOrThrow(contractRequest.getClient());
        User user = userService.getUserOrThrow(contractRequest.getUser());
        Currency currency = currencyService.getCurrencyOrThrow(contractRequest.getCurrency());
        WareHouse outPutWareHouse = wareHouseService.getWareHouseOrThrow(contractRequest.getOutputWareHouse());
        PayType payType = getPayTypeOrThrow(contractRequest.getPayType());
        Contract contract = mapper.toEntity(contractRequest, Contract.class);
        contract.addJoin(client, user, currency, outPutWareHouse, payType);
        contract.setContractNo(numberAutomatic.createDateTimeNo());
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
        PayType newPayType = getPayTypeOrThrow(contractRequest.getPayType());
        Contract newContract = mapper.toEntity(contractRequest, Contract.class);
        Contract findContract = getContractOrThrow(contractId);
        findContract.update(newContract, newClient, newUser, newCurrency, newOutPutWareHouse, newPayType);
        contractRepo.save(findContract);
        return mapper.toResponse(findContract, ContractResponse.class);
    }

    // 수주 삭제
    @Override
    public void deleteContract(Long id) throws NotFoundException, BadRequestException {
        Contract contract = getContractOrThrow(id);
        // 수주 삭제 시 해당하는 수주의 수주품목 정보가 존재하면 삭제 불가능
        throwIfContractExistsInContractItem(contract);
        // 수주 삭제 시 해당하는 수주가 제조오더에 등록되어 있으면 삭제 불가능
        throwIfContractExistsInProduceOrder(contract);

        contract.delete();
        contractRepo.save(contract);
//        List<ContractItem> contractItems = contractItemRepo.findAllByContractAndDeleteYnFalse(contract);
//        // 해당 수주에 해당하는 수주품목, 수주품목파일 같이 삭제
//        for (ContractItem contractItem : contractItems) {
//            // 수주품목 삭제
//            contractItem.delete();
//            contractItemRepo.save(contractItem);
//            // 수주품목 파일 삭제
//            List<ContractItemFile> itemFiles = contractItemFileRepo.findAllByContractItemAndDeleteYnFalse(contractItem);
//            itemFiles.forEach(ContractItemFile::delete);
//            contractItemFileRepo.saveAll(itemFiles);
//        }
    }

    // 수주 삭제 시 해당하는 수주의 수주품목 정보가 존재하면 삭제 불가능
    private void throwIfContractExistsInContractItem(Contract contract) throws BadRequestException {
        boolean exist = contractItemRepo.existsByContractAndDeleteYnFalse(contract);
        if (exist) throw new BadRequestException("수주에 해당하는 수주품목 정보가 존재하므로 삭제가 불가능합니다. 수주품목 삭제 후 다시 시도해주세요.");
    }

    // 수주 단일 조회 및 예외
    @Override
    public Contract getContractOrThrow(Long id) throws NotFoundException {
        return contractRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("contract does not exist. input id: " + id));
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
        ContractItem contractItem = getContractItemOrThrow(contractId, contractItemId);
        return mapper.toResponse(contractItem, ContractItemResponse.class);
    }

    // 수주 품목 전체 조회
    @Override
    public List<ContractItemResponse> getContractItems(Long contractId) throws NotFoundException {
        Contract contract = getContractOrThrow(contractId);
        List<ContractItem> contractItems = contractItemRepo.findAllByContractAndDeleteYnFalseOrderByCreatedDateDesc(contract);
        return mapper.toListResponses(contractItems, ContractItemResponse.class);
    }

    // 수주 품목 수정
    @Override
    public ContractItemResponse updateContractItem(Long contractId, Long contractItemId, ContractItemRequest contractItemRequest) throws NotFoundException {
        ContractItem findContractItem = getContractItemOrThrow(contractId, contractItemId);
        Item newItem = itemService.getItemOrThrow(contractItemRequest.getItem());
        ContractItem newContractItem = mapper.toEntity(contractItemRequest, ContractItem.class);
        findContractItem.update(newContractItem, newItem);
        contractItemRepo.save(findContractItem);
        return mapper.toResponse(findContractItem, ContractItemResponse.class);
    }

    // 수주 품목 삭제
    @Override
    public void deleteContractItem(Long contractId, Long contractItemId) throws NotFoundException, BadRequestException {
        ContractItem contractItem = getContractItemOrThrow(contractId, contractItemId);
        // 제조오더에 등록되어 있으면 삭제 불가능
        throwIfContractItemExistsInProduceOrder(contractItem);
        contractItem.delete();
        contractItemRepo.save(contractItem);
    }

    // 수주품목이 제조오더에 등록되어 있으면 삭제 불가능
    private void throwIfContractItemExistsInProduceOrder(ContractItem contractItem) throws BadRequestException {
        boolean exist = produceOrderRepository.existsByContractItemAndDeleteYnFalse(contractItem);
        if (exist) throw new BadRequestException("해당 수주품목이 제조오더에 등록되어 있으므로 삭제가 불가능합니다. 제조오더 삭제 후 다시 시도해주세요.");
    }

    // 수주가 제조오더에 등록되어 있으면 삭제 불가능
    private void throwIfContractExistsInProduceOrder(Contract contract) throws BadRequestException {
        boolean exist = produceOrderRepository.existsByContractAndDeleteYnFalse(contract);
        if (exist) throw new BadRequestException("해당 수주가 제조오더에 등록되어 있으므로 삭제가 불가능합니다. 제조오더 삭제 후 다시 시도해주세요.");
    }

    // 수주 품목 단일 조회 및 예외
    @Override
    public ContractItem getContractItemOrThrow(Long contractId, Long contractItemId) throws NotFoundException {
        Contract contract = getContractOrThrow(contractId);
        return contractItemRepo.findByIdAndContractAndDeleteYnFalse(contractItemId, contract)
                .orElseThrow(() -> new NotFoundException("contract item does not exist. input id: " + contractItemId));
    }

    // ======================================== 수주 품목 파일 ===============================================
    // 수주 품목 파일 추가
    // 파일명: contract-items/수주번호/고객발주번호/파일명(현재날짜)
    @Override
    public ContractItemFileResponse createBusinessFileToContractItemFile(Long contractId, Long contractItemId, MultipartFile file) throws NotFoundException, BadRequestException, IOException {
        Contract contract = getContractOrThrow(contractId);
        ContractItem contractItem = getContractItemOrThrow(contractId, contractItemId);

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
        ContractItem contractItem = getContractItemOrThrow(contractId, contractItemId);
        List<ContractItemFile> contractItemFiles = contractItemFileRepo.findAllByContractItemAndDeleteYnFalse(contractItem);
        return mapper.toListResponses(contractItemFiles, ContractItemFileResponse.class);
    }

    // 수주 품목 파일 삭제
    @Override
    public void deleteItemFile(Long contractId, Long contractItemId, Long contractItemFileId) throws NotFoundException {
        ContractItem contractItem = getContractItemOrThrow(contractId, contractItemId);
        ContractItemFile contractItemFile = contractItemFileRepo.findByIdAndContractItemAndDeleteYnFalse(contractItemFileId, contractItem);
        contractItemFile.delete();
        contractItemFileRepo.save(contractItemFile);
    }

    // 결제조건 단일 조회 및 예외
    private PayType getPayTypeOrThrow(Long id) throws NotFoundException {
        return payTypeRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("payType does not exist. input id: " + id));
    }
}
