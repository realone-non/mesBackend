package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WorkProcessService;
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

// 3-3-2. 작업 공정 코드 등록
@Tag(name = "work-process-code", description = "작업 공정 코드 API")
@RequestMapping("/work-process-codes")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class WorkProcessCodeController {

    @Autowired
    WorkProcessService workProcessCodeService;

    //  코드 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "작업공정 코드 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<CodeResponse> createWorkProcessCode(
            @RequestBody @Valid CodeRequest codeRequest
    ) {
        return new ResponseEntity<>(workProcessCodeService.createWorkProcessCode(codeRequest), HttpStatus.OK);
    }

    // 코드 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업공정 코드 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<CodeResponse> getWorkProcessCode(
            @PathVariable Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(workProcessCodeService.getWorkProcessCode(id), HttpStatus.OK);
    }

    // 코드 리스트 조회
    @GetMapping
    @ResponseBody()
    @Operation(summary = "작업공정 코드 리스트 조회")
    public ResponseEntity<List<CodeResponse>> getWorkProcessCodes() {
        return new ResponseEntity<>(workProcessCodeService.getWorkProcessCodes(), HttpStatus.OK);
    }

    // 코드 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업공정 코드 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteWorkProcessCode(
            @PathVariable Long id
    ) throws NotFoundException, BadRequestException {
        workProcessCodeService.deleteWorkProcessCode(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
