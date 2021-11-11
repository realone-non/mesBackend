package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ClientRequest;
import com.mes.mesBackend.dto.response.ClientResponse;
import com.mes.mesBackend.entity.ClientType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/clients")
@Api(tags = "client")
@RequiredArgsConstructor
public class ClientController {

    @Autowired
    ClientService clientService;

    // 거래처 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "거래처 생성")
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientRequest clientRequest) throws NotFoundException {
        return new ResponseEntity<>(clientService.createClient(clientRequest), HttpStatus.OK);
    }

    // 거래처 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "거래처 단일 조회")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(clientService.getClient(id), HttpStatus.OK);
    }

    // 거래처 조건 페이징 조회 (거래처 유형, 거래처 코드, 거래처 명)
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "거래처 리스트 조회", notes = "검색조건 : 거래처 유형, 거래처 코드, 거래처 명")
    public ResponseEntity<Page<ClientResponse>> getClients(
            @RequestParam(required = false) Long clientType,
            @RequestParam(required = false) String clientCode,
            @RequestParam(required = false) String name,
            @PageableDefault Pageable pageable
    ) {
        return new ResponseEntity<>(clientService.getClients(clientType, clientCode, name, pageable), HttpStatus.OK);
    }

    // 거래처 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "거래처 수정")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @RequestBody ClientRequest clientRequest) throws NotFoundException {
        return new ResponseEntity<>(clientService.updateClient(id, clientRequest), HttpStatus.OK);
    }

    // 거래처 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "거래처 삭제")
    public ResponseEntity deleteClient(@PathVariable Long id) throws NotFoundException {
        clientService.deleteClient(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 사업자 등록증 파일 업로드
    @PutMapping("/{id}/files")
    @ResponseBody
    @ApiOperation(value = "사업자등록증 파일 생성")
    public ResponseEntity<ClientResponse> createBusinessFileToClient(
            @PathVariable Long id,
            @RequestPart MultipartFile businessFile
    ) throws NotFoundException, IOException, BadRequestException {
        return new ResponseEntity<>(clientService.createBusinessFileToClient(id, businessFile), HttpStatus.OK);
    }
}
