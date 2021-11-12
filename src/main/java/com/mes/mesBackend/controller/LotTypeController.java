package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.LotTypeRequest;
import com.mes.mesBackend.dto.response.LotTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.LotTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// Lot 유형
@RestController
@RequestMapping(value = "/lot-types")
@Api(tags = "lot-type")
@RequiredArgsConstructor
public class LotTypeController {
    @Autowired
    LotTypeService lotTypeService;

    // LOT 유형 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "LOT 유형 생성")
    public ResponseEntity<LotTypeResponse> createLotType(
            @RequestBody @Valid LotTypeRequest lotTypeRequest
    ) {
        return new ResponseEntity<>(lotTypeService.createLotType(lotTypeRequest), HttpStatus.OK);
    }

    // LOT 유형 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "LOT 유형 단일 조회")
    public ResponseEntity<LotTypeResponse> getLotType(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(lotTypeService.getLotType(id), HttpStatus.OK);
    }

    // LOT 유형 리스트 조회
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "LOT 유형 리스트 조회")
    public ResponseEntity<List<LotTypeResponse>> getLotTypes() {
        return new ResponseEntity<>(lotTypeService.getLotTypes(), HttpStatus.OK);
    }

    // LOT 유형 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "LOT 유형 수정")
    public ResponseEntity<LotTypeResponse> updateLotType(
            @PathVariable Long id,
            @RequestBody @Valid LotTypeRequest lotTypeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(lotTypeService.updateLotType(id, lotTypeRequest), HttpStatus.OK);
    }

    // LOT 유형 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "LOT 유형 삭제")
    public ResponseEntity deleteLotType(
            @PathVariable Long id
    ) throws NotFoundException {
        lotTypeService.deleteLotType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
