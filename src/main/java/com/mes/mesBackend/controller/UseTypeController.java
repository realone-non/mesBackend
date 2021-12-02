package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.UseTypeRequest;
import com.mes.mesBackend.dto.response.UseTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.UseTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 용도유형
@Tag(name = "use-type", description = "용도유형 API")
@RequestMapping(value = "/use-types")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class UseTypeController {
    @Autowired
    UseTypeService useTypeService;

    // 용도유형 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "용도유형 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<UseTypeResponse> createUseType(
            @RequestBody @Valid UseTypeRequest useTypeRequest
    ) {
        return new ResponseEntity<>(useTypeService.createUseType(useTypeRequest), HttpStatus.OK);
    }

    // 용도유형 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "용도유형 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<UseTypeResponse> getUseType(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(useTypeService.getUseType(id), HttpStatus.OK);
    }

    // 용도유형 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "용도유형 리스트 조회")
    public ResponseEntity<List<UseTypeResponse>> getUseTypes() {
        return new ResponseEntity<>(useTypeService.getUseTypes(), HttpStatus.OK);
    }

    // 용도유형 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "용도유형 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<UseTypeResponse> updateUseType(
            @PathVariable Long id,
            @RequestBody @Valid UseTypeRequest useTypeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(useTypeService.updateUseType(id, useTypeRequest), HttpStatus.OK);
    }

    // 용도유형 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "용도유형 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteUseType(
            @PathVariable Long id
    ) throws NotFoundException {
        useTypeService.deleteUseType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
