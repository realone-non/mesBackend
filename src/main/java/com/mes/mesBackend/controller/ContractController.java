package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ContractItemRequest;
import com.mes.mesBackend.dto.request.ContractRequest;
import com.mes.mesBackend.dto.response.ContractItemFileResponse;
import com.mes.mesBackend.dto.response.ContractItemResponse;
import com.mes.mesBackend.dto.response.ContractResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

// 4-2. 수주 등록
@RequestMapping(value = "/contracts")
@Tag(name = "contract", description = "4-2. 수주 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@Slf4j
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ContractController.class);
    private CustomLogger cLogger;

    // ======================================== 수주 ===============================================
    // 수주 생성
    @Operation(summary = "수주 생성", description = "")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ContractResponse> createContract(
            @RequestBody @Valid ContractRequest contractRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ContractResponse contract = contractService.createContract(contractRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + contract.getId() + " from createContract.");
        return new ResponseEntity<>(contract, OK);
    }

    // 수주 단일 조회
    @GetMapping("/{contract-id}")
    @ResponseBody()
    @Operation(summary = "수주 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ContractResponse> getContract(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ContractResponse contract = contractService.getContract(contractId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + contract.getId() + " from getContract.");
        return new ResponseEntity<>(contract, OK);
    }

    // 수주 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "수주 리스트 조회", description = "검색조건: 거래처 명, 수주기간 fromDate~toDate, 화폐 id, 담당자 명")
    public ResponseEntity<List<ContractResponse>> getContracts(
            @RequestParam(required = false) @Parameter(description = "거래처 명") String clientName,
            @RequestParam(required = false) @Parameter(description = "담당자 명") String userName,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "수주기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "수주기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "화폐 id") Long currencyId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ContractResponse> contracts = contractService.getContracts(clientName, userName, fromDate, toDate, currencyId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of currencyId: " + currencyId + " from getContracts.");
        return new ResponseEntity<>(contracts, OK);
    }

    // 수주 수정
    @PatchMapping("/{contract-id}")
    @ResponseBody()
    @Operation(summary = "수주 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ContractResponse> updateContract(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @RequestBody @Valid ContractRequest contractRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ContractResponse contract = contractService.updateContract(contractId, contractRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + contract.getId() + " from updateContract.");
        return new ResponseEntity<>(contract, OK);
    }

    // 수주 삭제
    @DeleteMapping("/{contract-id}")
    @ResponseBody()
    @Operation(summary = "수주 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteContract(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        contractService.deleteContract(contractId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + contractId + " from deleteContract.");
        return new ResponseEntity(NO_CONTENT);
    }

    // ======================================== 수주 품목 ===============================================
    // 수주 품목 생성
    @Operation(summary = "수주 품목 생성", description = "")
    @PostMapping("/{contract-id}/contract-items")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ContractItemResponse> createContractItem(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @RequestBody @Valid ContractItemRequest contractItemRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ContractItemResponse contractItem = contractService.createContractItem(contractId, contractItemRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + contractItem.getId() + " from createContractItem.");
        return new ResponseEntity<>(contractItem, OK);
    }

    // 수주 품목 단일 조회
    @GetMapping("/{contract-id}/contract-items/{contract-item-id}")
    @ResponseBody
    @Operation(summary = "수주 품목 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<ContractItemResponse> getContractItem(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @PathVariable(value = "contract-item-id") @Parameter(description = "수주 품목 id") Long contractItemId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ContractItemResponse contractItem = contractService.getContractItem(contractId, contractItemId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + contractItem.getId() + " from getContractItem.");
        return new ResponseEntity<>(contractItem, OK);
    }

    // 수주 품목 전체 조회
    @GetMapping("/{contract-id}/contract-items")
    @ResponseBody
    @Operation(summary = "수주 품목 전체 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<ContractItemResponse>> getContractItems(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ContractItemResponse> contractItems = contractService.getContractItems(contractId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of contractId: " + contractId + " from getContractItems.");
        return new ResponseEntity<>(contractItems, OK);
    }

    // 수주 품목 수정
    @PatchMapping("/{contract-id}/contract-items/{contract-item-id}")
    @ResponseBody()
    @Operation(summary = "수주 품목 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ContractItemResponse> updateContractItem(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @PathVariable(value = "contract-item-id") @Parameter(description = "수주 품목 id") Long contractItemId,
            @RequestBody @Valid ContractItemRequest contractItemRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ContractItemResponse contractItem = contractService.updateContractItem(contractId, contractItemId, contractItemRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + contractItem.getId() + " from updateContractItem.");
        return new ResponseEntity<>(contractItem, OK);
    }

    // 수주 품목 삭제
    @DeleteMapping("/{contract-id}/contract-items/{contract-item-id}")
    @ResponseBody()
    @Operation(summary = "수주 품목 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteContractItem(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @PathVariable(value = "contract-item-id") @Parameter(description = "수주 품목 id") Long contractItemId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        contractService.deleteContractItem(contractId, contractItemId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + contractId + " from deleteContractItem.");
        return new ResponseEntity(NO_CONTENT);
    }
    
    // ======================================== 수주 품목 파일 ===============================================
    // 수주 품목 파일 추가
    @PutMapping(value = "/{contract-id}/contract-items/{contract-item-id}/contract-item-files",  consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "수주 품목 파일 추가", hidden = true)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ContractItemFileResponse> createFileToContractItem(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @PathVariable(value = "contract-item-id") @Parameter(description = "수주 품목 id") Long contractItemId,
            @RequestPart MultipartFile file,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, IOException, BadRequestException {
        ContractItemFileResponse fileToContractItemFile = contractService.createBusinessFileToContractItemFile(contractId,contractItemId, file);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + fileToContractItemFile + " from createFileToContractItem.");
        return new ResponseEntity<>(fileToContractItemFile, OK);
    }

    // 수주 품목 파일 전체 조회
    @GetMapping("/{contract-id}/contract-items/{contract-item-id}/contract-item-files")
    @ResponseBody
    @Operation(summary = "수주 품목 파일 전체 조회", hidden = true)
    public ResponseEntity<List<ContractItemFileResponse>> getContractItemFiles(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @PathVariable(value = "contract-item-id") @Parameter(description = "수주 품목 id") Long contractItemId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ContractItemFileResponse> itemFiles = contractService.getItemFiles(contractId, contractItemId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getContractItemFiles.");
        return new ResponseEntity<>(itemFiles, OK);
    }
    
    // 수주 품목 파일 삭제
    @DeleteMapping("/{contract-id}/contract-items/{contract-item-id}/contract-item-files/{contract-item-file-id}")
    @ResponseBody
    @Operation(summary = "수주 품목 파일 삭제", hidden = true)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteContractItemFile(
            @PathVariable(value = "contract-id") @Parameter(description = "수주 id") Long contractId,
            @PathVariable(value = "contract-item-id") @Parameter(description = "수주 품목 id") Long contractItemId,
            @PathVariable(value = "contract-item-file-id") @Parameter(description = "수주 품목 파일 id") Long contractItemFileId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        contractService.deleteItemFile(contractId, contractItemId, contractItemFileId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + contractItemFileId + " from deleteContractItemFile.");
        return new ResponseEntity(NO_CONTENT);
    }
}