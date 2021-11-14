package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WorkCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



// 3-3-1. 작업장 코드 등록
@Tag(name = "work-center-code", description = "작업장 코드 API")
@RequestMapping("/work-center-codes")
@RestController
@RequiredArgsConstructor
public class WorkCenterCodeController {

    @Autowired
    WorkCenterService workCenterService;

    //  코드 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "작업장 코드 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<CodeResponse> createWorkCenterCode(
            @RequestBody @Valid CodeRequest codeRequest
    ) {
        return new ResponseEntity<>(workCenterService.createWorkCenterCode(codeRequest), HttpStatus.OK);
    }

    // 코드 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업장 코드 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<CodeResponse> getWorkCenterCode(
            @PathVariable Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterService.getWorkCenterCode(id), HttpStatus.OK);
    }

    // 코드 전체 조회
    @GetMapping
    @ResponseBody()
    @Operation(summary = "작업장 코드 전체 조회")
    public ResponseEntity<List<CodeResponse>> getWorkCenterCodes() {
        return new ResponseEntity<>(workCenterService.getWorkCenterCodes(), HttpStatus.OK);
    }

    // 코드 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업장 코드 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteWorkCenterCode(
            @PathVariable Long id
    ) throws NotFoundException, BadRequestException {
        workCenterService.deleteWorkCenterCode(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
