package com.mes.mesBackend.controller;


import com.mes.mesBackend.dto.request.ClientTypeRequest;
import com.mes.mesBackend.dto.response.ClientTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ClientTypeService;
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

import javax.validation.Valid;
import java.util.List;

@Tag(name = "client-type", description = "거래처 유형 API")
@RequestMapping(value = "/client-types")
@RestController
@RequiredArgsConstructor
public class ClientTypeController {

    @Autowired
    ClientTypeService clientTypeService;

    // 거래처 유형 생성
    @PostMapping
    @ResponseBody()
    @Operation(summary = "거래처유형 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ClientTypeResponse> createClientType(
            @RequestBody @Valid ClientTypeRequest clientTypeRequest
    ) {
        return new ResponseEntity<>(clientTypeService.createClientType(clientTypeRequest), HttpStatus.OK);
    }

    // 거래처 유형 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "거래처유형 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<ClientTypeResponse> getClientType(
            @PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(clientTypeService.getClientType(id), HttpStatus.OK);
    }

    // 거래처 유형 전체 조회
    @Operation(summary = "거래처유형 전체 조회")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ClientTypeResponse>> getClientTypes() {
        return new ResponseEntity<>(clientTypeService.getClientTypes(), HttpStatus.OK);
    }

    // 거래처 유형 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "거래처유형 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ClientTypeResponse> updateClientType(
            @PathVariable Long id,
            @RequestBody @Valid ClientTypeRequest clientTypeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(clientTypeService.updateClientType(id, clientTypeRequest), HttpStatus.OK);
    }

    // 거래처유형 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "거래처유형 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteClientType(@PathVariable Long id) throws NotFoundException {
        clientTypeService.deleteClientType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @GetMapping
//    @ResponseBody()
//    @Operation(summary = "거래처유형 페이징 조회")
//    @Parameters(
//            value = {
//                    @Parameter(
//                            name = "page", description = "0 부터 시작되는 페이지 (0..N)",
//                            in = ParameterIn.QUERY,
//                            schema = @Schema(type = "integer", defaultValue = "0")
//                    ),
//                    @Parameter(
//                            name = "size", description = "페이지의 사이즈",
//                            in = ParameterIn.QUERY,
//                            schema = @Schema(type = "integer", defaultValue = "20")
//                    ),
//                    @Parameter(
//                            name = "sort", in = ParameterIn.QUERY,
//                            description = "정렬할 대상과 정렬 방식, 데이터 형식: property(,asc|desc). + 디폴트 정렬순서는 오름차순, 다중정렬 가능",
//                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,desc"))
//                    )
//            }
//    )
//    public ResponseEntity<Page<ClientTypeResponse>> getClientTypes(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(clientTypeService.getClientTypes(pageable), HttpStatus.OK);
//    }

}
