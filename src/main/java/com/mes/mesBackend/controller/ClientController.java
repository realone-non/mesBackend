package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ClientRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Tag(name = "client", description = "거래처 API")
@RequestMapping(value = "/clients")
@RestController
@RequiredArgsConstructor
public class ClientController {

    @Autowired
    ClientService clientService;

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
            @RequestBody @Valid ClientRequest clientRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(clientService.createClient(clientRequest), HttpStatus.OK);
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
    public ResponseEntity<ClientResponse> getClient(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(clientService.getClient(id), HttpStatus.OK);
    }

    // 거래처 조건 페이징 조회 (거래처 유형, 거래처 코드, 거래처 명)
    @GetMapping
    @ResponseBody
    @Operation(summary = "거래처 페이징 조회", description = "검색조건 : 거래처 유형, 거래처 코드, 거래처 명")
    @Parameters(
            value = {
                    @Parameter(
                            name = "page", description = "0 부터 시작되는 페이지 (0..N)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "0")
                    ),
                    @Parameter(
                            name = "size", description = "페이지의 사이즈",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "20")
                    ),
                    @Parameter(
                            name = "sort", in = ParameterIn.QUERY,
                            description = "정렬할 대상과 정렬 방식, 데이터 형식: property(,asc|desc). + 디폴트 정렬순서는 오름차순, 다중정렬 가능",
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,desc"))
                    )
            }
    )
    public ResponseEntity<Page<ClientResponse>> getClients(
            @RequestParam(required = false) @Parameter(description = "거래처 유형 id") Long clientType,
            @RequestParam(required = false) @Parameter(description = "거래처 코드 id") String clientCode,
            @RequestParam(required = false) @Parameter(description = "거래처 명") String name,
            @PageableDefault @Parameter(hidden = true) Pageable pageable
    ) {
        return new ResponseEntity<>(clientService.getClients(clientType, clientCode, name, pageable), HttpStatus.OK);
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
            @RequestBody @Valid ClientRequest clientRequest) throws NotFoundException {
        return new ResponseEntity<>(clientService.updateClient(id, clientRequest), HttpStatus.OK);
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
    public ResponseEntity deleteClient(@PathVariable Long id) throws NotFoundException {
        clientService.deleteClient(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 사업자 등록증 파일 업로드
    @PutMapping("/{id}/files")
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
            @RequestPart MultipartFile businessFile
    ) throws NotFoundException, IOException, BadRequestException {
        return new ResponseEntity<>(clientService.createBusinessFileToClient(id, businessFile), HttpStatus.OK);
    }
}
