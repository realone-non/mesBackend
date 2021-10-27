package com.mes.mesBackend.controller;


import com.mes.mesBackend.dto.request.ClientTypeRequest;
import com.mes.mesBackend.dto.response.ClientTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ClientTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/client-types")
@Api(tags = "client-type")
@RequiredArgsConstructor
public class ClientTypeController {

    @Autowired
    ClientTypeService clientTypeService;

    // 거래처 유형 생성
    @PostMapping
    @ResponseBody()
    @ApiOperation(value = "거래처유형 생성")
    public ResponseEntity<ClientTypeResponse> createClientType(
            @RequestBody ClientTypeRequest clientTypeRequest
    ) {
        return new ResponseEntity<>(clientTypeService.createClientType(clientTypeRequest), HttpStatus.OK);
    }

    // 거래처 유형 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "거래처유형 조회")
    public ResponseEntity<ClientTypeResponse> getClientType(@PathVariable(value = "id") Long id) throws NotFoundException {
        return new ResponseEntity<>(clientTypeService.getClientType(id), HttpStatus.OK);
    }

    // 거래처 유형 페이징 조회
    @GetMapping
    @ResponseBody()
    @ApiOperation(value = "거래처유형 페이징 조회")
    public ResponseEntity<Page<ClientTypeResponse>> getClientTypes(Pageable pageable) {
        return new ResponseEntity<>(clientTypeService.getClientTypes(pageable), HttpStatus.OK);
    }

    // 거래처 유형 수정
    @PutMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "거래처유형 수정")
    public ResponseEntity<ClientTypeResponse> updateClientType(
            @PathVariable(value = "id") Long id,
            @RequestBody ClientTypeRequest clientTypeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(clientTypeService.updateClientType(id, clientTypeRequest), HttpStatus.OK);
    }

    // 거래처유형 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @ApiOperation(value = "거래처유형 삭제")
    public ResponseEntity<Void> deleteClientType(@PathVariable(value = "id") Long id) throws NotFoundException {
        clientTypeService.deleteClientType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
