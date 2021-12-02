package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.EstimateItemRequest;
import com.mes.mesBackend.dto.request.EstimatePiRequest;
import com.mes.mesBackend.dto.request.EstimateRequest;
import com.mes.mesBackend.dto.response.EstimateItemResponse;
import com.mes.mesBackend.dto.response.EstimatePiResponse;
import com.mes.mesBackend.dto.response.EstimateResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.EstimateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "estimate", description = "견적 API")
@RequestMapping("/estimates")
@RestController
@SecurityRequirement(name = "Authorization")
public class EstimateController {
    @Autowired
    EstimateService estimateService;

    // 견적 생성
    @Operation(summary = "견적 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimateResponse> createEstimate(
            @RequestBody @Valid EstimateRequest estimateRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(estimateService.createEstimate(estimateRequest), HttpStatus.OK);
    }

    // 견적 단일 조회
    @Operation(summary = "견적 단일 조회")
    @GetMapping("/{estimate-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<EstimateResponse> getEstimate(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId
    ) throws NotFoundException {
        return new ResponseEntity<>(estimateService.getEstimate(estimateId), HttpStatus.OK);
    }

    // 견적 전체 조회 검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 거래처 담당자
    @Operation(summary = "견적 전체 조회", description = "검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 거래처 담당자")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<EstimateResponse>> getEstimates(
            @RequestParam(required = false) @Parameter(description = "거래처") String clientName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "견적기간 fromDate") LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "견적기간 toDate") LocalDate toDate,
            @RequestParam(required = false) @Parameter(description = "화폐 id") Long currencyId,
            @RequestParam(required = false) @Parameter(description = "거래처 담당자 명") String chargeName
    ) {
        return new ResponseEntity<>(estimateService.getEstimates(clientName, fromDate, toDate, currencyId, chargeName), HttpStatus.OK);
    }

    // 견적 수정
    @Operation(summary = "견적 수정")
    @PatchMapping("/{estimate-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimateResponse> updateEstimate(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @RequestBody @Valid EstimateRequest estimateRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(estimateService.updateEstimate(estimateId, estimateRequest), HttpStatus.OK);
    }

    // 견적 삭제
    @DeleteMapping("/{estimate-id}")
    @Operation(summary = "견적 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteEstimate(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId
    ) throws NotFoundException {
        estimateService.deleteEstimate(estimateId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 견적 페이징 조회 검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 담당자
//    @Operation(summary = "견적 페이징 조회", description = "검색조건: 거래처, 견적기간(startDate~endDate), 화폐, 담당자")
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
//    public ResponseEntity<Page<EstimateResponse>> getEstimates(
//            @RequestParam(required = false) @Parameter(description = "거래처") String clientName,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "견적기간 fromDate") LocalDate fromDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "견적기간 toDate") LocalDate toDate,
//            @RequestParam(required = false) @Parameter(description = "화폐 id") Long currencyId,
//            @RequestParam(required = false) @Parameter(description = "담당자 명") String chargeName,
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(estimateService.getEstimates(clientName, fromDate, toDate, currencyId, chargeName, pageable), HttpStatus.OK);
//    }

    // ===================================== 견적 품목 정보 ======================================

    @Operation(summary = "견적 품목정보 생성")
    @PostMapping("/{estimate-id}/estimate-items")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimateItemResponse> createEstimateItem(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @RequestBody @Valid EstimateItemRequest estimateItemRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(estimateService.createEstimateItem(estimateId, estimateItemRequest), HttpStatus.OK);
    }
    
    @Operation(summary = "견적 품목정보 단일 조회")
    @GetMapping("/{estimate-id}/estimate-items/{estimate-item-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimateItemResponse> getEstimateItem(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @PathVariable(value = "estimate-item-id") @Parameter(description = "견적 품목 id") Long estimateItemId
    ) throws NotFoundException {
        return new ResponseEntity<>(estimateService.getEstimateItem(estimateId, estimateItemId), HttpStatus.OK);
    }


    @Operation(summary = "견적 품목정보 리스트 조회",description = "해당하는 견적에 대한 모든 품목 조회")
    @GetMapping("/{estimate-id}/estimate-items")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<EstimateItemResponse>> getEstimateItems(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId
    ) throws NotFoundException {
        return new ResponseEntity<>(estimateService.getEstimateItems(estimateId), HttpStatus.OK);
    }
    
    @Operation(summary = "견적 품목정보 수정")
    @PatchMapping("/{estimate-id}/estimate-items/{estimate-item-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimateItemResponse> updateEstimateItem(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @PathVariable(value = "estimate-item-id") @Parameter(description = "견적 품목 id") Long estimateItemId,
            @RequestBody @Valid EstimateItemRequest estimateItemRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(estimateService.updateEstimateItem(estimateId, estimateItemId, estimateItemRequest), HttpStatus.OK);
    }

    @Operation(summary = "견적 품목정보 삭제")
    @DeleteMapping("/{estimate-id}/estimate-items/{estimate-item-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity deleteEstimateItem(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @PathVariable(value = "estimate-item-id") @Parameter(description = "견적 품목 id") Long estimateItemId
    ) throws NotFoundException {
        estimateService.deleteEstimateItem(estimateId, estimateItemId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

// ===================================== 견적 P/I ======================================
    // p/i
    /*
    * - invoice No 자동생성인지?
    *  널 허용 여부랑
    * 견적이랑 1:1인지
    * */
    @Operation(summary = "견적 P/I 생성")
    @PostMapping("/{estimate-id}/pis")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimatePiResponse> createEstimatePi(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @RequestBody @Valid EstimatePiRequest estimatePiRequest
    ) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(estimateService.createEstimatePi(estimateId, estimatePiRequest), HttpStatus.OK);
    }

    @Operation(summary = "견적 P/I 조회")
    @GetMapping("/{estimate-id}/pis")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimatePiResponse> getEstimatePi(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId
    ) throws NotFoundException {
        return new ResponseEntity<>(estimateService.getEstimatePi(estimateId), HttpStatus.OK);
    }

    @Operation(summary = "견적 P/I 수정")
    @PatchMapping("/{estimate-id}/pis/{pi-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimatePiResponse> updateEstimatePi(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @PathVariable(value = "pi-id") @Parameter(description = "견적 P/I id") Long estimatePiId,
            @RequestBody @Valid EstimatePiRequest estimatePiRequest
    ) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(estimateService.updateEstimatePi(estimateId, estimatePiId, estimatePiRequest), HttpStatus.OK);
    }

    @Operation(summary = "견적 P/I 삭제")
    @DeleteMapping("/{estimate-id}/pis/{pi-id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<EstimatePiResponse> deleteEstimatePi(
            @PathVariable(value = "estimate-id") @Parameter(description = "견적 id") Long estimateId,
            @PathVariable(value = "pi-id") @Parameter(description = "견적 P/I id") Long estimatePiId
    ) throws NotFoundException, BadRequestException {
        estimateService.deleteEstimatePi(estimateId, estimatePiId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
