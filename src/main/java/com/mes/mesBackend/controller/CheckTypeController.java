package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CheckTypeRequest;
import com.mes.mesBackend.dto.response.CheckTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.CheckTypeService;
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

// 점검유형 (일, 월, 분기 등등)
@Tag(name = "check-type", description = "점검유형 API")
@RequestMapping("/check-types")
@RestController
@RequiredArgsConstructor
public class CheckTypeController {

    @Autowired
    CheckTypeService checkTypeService;

    // 점검유형 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "점검유형 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<CheckTypeResponse> createCheckType(
            @RequestBody @Valid CheckTypeRequest checkTypeRequest
    ) {
        return new ResponseEntity<>(checkTypeService.createCheckType(checkTypeRequest), HttpStatus.OK);
    }

    // 점검유형 단일조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "점검유형 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<CheckTypeResponse> getCheckType(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(checkTypeService.getCheckType(id), HttpStatus.OK);
    }

    // 점검유형 전체 조회
    @GetMapping
    @ResponseBody()
    @Operation(summary = "점검유형 전체 조회")
    public ResponseEntity<List<CheckTypeResponse>> getCheckTypes() {
        return new ResponseEntity<>(checkTypeService.getCheckTypes(), HttpStatus.OK);
    }

    // 점검유형 수정 api
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "점검유형 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<CheckTypeResponse> updateCheckType(
            @PathVariable Long id,
            @RequestBody @Valid CheckTypeRequest checkTypeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(checkTypeService.updateCheckType(id, checkTypeRequest), HttpStatus.OK);
    }

    // 점검유형 삭제 api
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "점검유형 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteCheckType(@PathVariable Long id) throws NotFoundException {
        checkTypeService.deleteCheckType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
