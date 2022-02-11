package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ClientRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.NO_CONTENT;
import static org.springframework.http.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

// 3-1-6. 거래처 등록
@Tag(name = "client", description = "3-1-6. 거래처 등록 API")
@RequestMapping(value = "/clients")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class ClientController {
    private final ClientService clientService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private CustomLogger cLogger;

    // 거래처 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "거래처 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ClientResponse> createClient(
            @RequestBody @Valid ClientRequest clientRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ClientResponse client = clientService.createClient(clientRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + client.getId() + " from createClient.");
        return new ResponseEntity<>(client, OK);
    }

    // 거래처 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "거래처 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<ClientResponse> getClient(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ClientResponse client = clientService.getClient(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + client.getId() + " from getClient.");
        return new ResponseEntity<>(client, OK);
    }

    // 거래처 전체 조회 (거래처 유형, 거래처 코드, 거래처 명)
    @GetMapping
    @ResponseBody
    @Operation(summary = "거래처 전체 조회", description = "검색조건 : 거래처 유형, 거래처 코드, 거래처 명")
    public ResponseEntity<List<ClientResponse>> getClients(
            @RequestParam(required = false) @Parameter(description = "거래처 유형 id") Long clientType,
            @RequestParam(required = false) @Parameter(description = "거래처 코드 id") String clientCode,
            @RequestParam(required = false) @Parameter(description = "거래처 명") String name,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ClientResponse> clients = clientService.getClients(clientType, clientCode, name);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of clientTypeId: "+ clientType + " from getClients.");
        return new ResponseEntity<>(clients, OK);
    }

    // 거래처 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "거래처 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id,
            @RequestBody @Valid ClientRequest clientRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ClientResponse client = clientService.updateClient(id, clientRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + client.getId() + " from updateClient.");
        return new ResponseEntity<>(client, OK);
    }

    // 거래처 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "거래처 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteClient(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        clientService.deleteClient(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteClient.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 사업자 등록증 파일 업로드
    @PutMapping(value = "/{id}/files",  consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "사업자등록증 파일 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ClientResponse> createBusinessFileToClient(
            @PathVariable Long id,
            @RequestPart MultipartFile businessFile,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, IOException, BadRequestException {
        ClientResponse fileToClient = clientService.createBusinessFileToClient(id, businessFile);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + fileToClient.getId() + " from createBusinessFileToClient.");
        return new ResponseEntity<>(fileToClient, OK);
    }
}
