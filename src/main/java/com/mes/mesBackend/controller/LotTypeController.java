package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.LotTypeRequest;
import com.mes.mesBackend.dto.response.LotTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.LotTypeService;
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

// Lot 유형
@Tag(name = "lot-type", description = "LOT 유형 API")
@RequestMapping(value = "/lot-types")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class LotTypeController {
    @Autowired
    LotTypeService lotTypeService;

    // LOT 유형 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "LOT 유형 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<LotTypeResponse> createLotType(
            @RequestBody @Valid LotTypeRequest lotTypeRequest
    ) {
        return new ResponseEntity<>(lotTypeService.createLotType(lotTypeRequest), HttpStatus.OK);
    }

    // LOT 유형 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "LOT 유형 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<LotTypeResponse> getLotType(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(lotTypeService.getLotType(id), HttpStatus.OK);
    }

    // LOT 유형 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "LOT 유형 리스트 조회")
    public ResponseEntity<List<LotTypeResponse>> getLotTypes() {
        return new ResponseEntity<>(lotTypeService.getLotTypes(), HttpStatus.OK);
    }

    // LOT 유형 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "LOT 유형 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<LotTypeResponse> updateLotType(
            @PathVariable Long id,
            @RequestBody @Valid LotTypeRequest lotTypeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(lotTypeService.updateLotType(id, lotTypeRequest), HttpStatus.OK);
    }

    // LOT 유형 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "LOT 유형 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteLotType(
            @PathVariable Long id
    ) throws NotFoundException {
        lotTypeService.deleteLotType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
