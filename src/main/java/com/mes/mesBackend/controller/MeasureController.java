package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.MeasureRequest;
import com.mes.mesBackend.dto.response.MeasureResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.MeasureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 3-5-3. 계측기 등록
@Tag(name = "measure", description = "계측기 API")
@RequestMapping("/measures")
@RestController
public class MeasureController {
    @Autowired
    MeasureService measureService;

    // 계측기 생성
    @Operation(summary = "계측기 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<MeasureResponse> createMeasure(
            @RequestBody @Valid MeasureRequest measureRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(measureService.createMeasure(measureRequest), HttpStatus.OK);
    }

    // 계측기 단일 조회
    @Operation(summary = "계측기 단일 조회")
    @GetMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<MeasureResponse> getMeasure(
            @PathVariable @Parameter(description = "계측기 id") Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(measureService.getMeasure(id), HttpStatus.OK);
    }

    // 계측기 전체 조회 검색조건: 검색조건: GAUGE유형, 검교정대상(월)
    @Operation(summary = "계측기 전체 조회", description = "검색조건: GAUGE유형, 검교정대상(월)")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<MeasureResponse>> getMeasures(
            @RequestParam(required = false) @Parameter(description = "GAUGE유형 id") Long gaugeId,
            @RequestParam(required = false) @Parameter(description = "월") Long month
    ) {
        return new ResponseEntity<>(measureService.getMeasures(gaugeId, month), HttpStatus.OK);
    }

    // 계측기 수정
    @Operation(summary = "계측기 수정")
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<MeasureResponse> updateMeasure(
            @PathVariable @Parameter(description = "계측기 id") Long id,
            @RequestBody @Valid MeasureRequest measureRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(measureService.updateMeasure(id, measureRequest), HttpStatus.OK);
    }

    // 계측기 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "계측기 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteMeasure(@PathVariable @Parameter(description = "계측기 id") Long id) throws NotFoundException {
        measureService.deleteMeasure(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 계측기 페이징 조회 검색조건: 검색조건: GAUGE유형, 검교정대상(월)
//    @Operation(summary = "계측기 페이징 조회", description = "검색조건: GAUGE유형, 검교정대상(월)")
//    @GetMapping
//    @ResponseBody
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
//    public ResponseEntity<Page<MeasureResponse>> getMeasures(
//            @RequestParam(required = false) @Parameter(description = "GAUGE유형 id") Long gaugeId,
//            @RequestParam(required = false) @Parameter(description = "월") Long month,
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(measureService.getMeasures(gaugeId, month, pageable), HttpStatus.OK);
//    }
}
