package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.GaugeTypeRequest;
import com.mes.mesBackend.dto.response.GaugeTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.GaugeTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "gauge-type", description = "GAUGE 유형 API")
@RequestMapping("/gauge-types")
@RestController
public class GaugeTypeController {
    @Autowired
    GaugeTypeService gaugeTypeService;

    // GAUGE 유형 생성
    @Operation(summary = "GAUGE 유형 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<GaugeTypeResponse> createGaugeType(
            @RequestBody @Valid GaugeTypeRequest gaugeTypeRequest
    ) {
        return new ResponseEntity<>(gaugeTypeService.createGaugeType(gaugeTypeRequest), HttpStatus.OK);
    }

    // GAUGE 유형 단일 조회
    @Operation(summary = "GAUGE 유형 단일 조회")
    @GetMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<GaugeTypeResponse> getGaugeType(
            @PathVariable @Parameter(description = "GAUGE 유형 id") Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(gaugeTypeService.getGaugeType(id), HttpStatus.OK);
    }

    // GAUGE 유형 리스트 조회
    @Operation(summary = "GAUGE 유형 리스트 조회", description = "")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<GaugeTypeResponse>> getGaugeTypes() {
        return new ResponseEntity<>(gaugeTypeService.getGaugeTypes(), HttpStatus.OK);
    }

    // GAUGE 유형 수정
    @Operation(summary = "GAUGE 유형 수정")
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<GaugeTypeResponse> updateGaugeType(
            @PathVariable @Parameter(description = "GAUGE 유형 id") Long id,
            @RequestBody @Valid GaugeTypeRequest gaugeTypeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(gaugeTypeService.updateGaugeType(id, gaugeTypeRequest), HttpStatus.OK);
    }

    // GAUGE 유형 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "GAUGE 유형 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteGaugeType(@PathVariable @Parameter(description = "GAUGE 유형 id") Long id) throws NotFoundException {
        gaugeTypeService.deleteGaugeType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
