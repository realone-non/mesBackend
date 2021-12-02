package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkCenterCheckDetailRequest;
import com.mes.mesBackend.dto.response.WorkCenterCheckDetailResponse;
import com.mes.mesBackend.dto.response.WorkCenterCheckResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WorkCenterCheckService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 3-3-4. 작업장별 점검 항목 등록
@Tag(name = "work-center-check", description = "작업장별 점검 항목 API")
@RequestMapping("/work-center-checks")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class WorkCenterCheckController {

    @Autowired
    WorkCenterCheckService workCenterCheckService;

    // 작업장별 점검유형 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "작업장별 점검유형 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkCenterCheckResponse> createWorkCenterCheck(
            @RequestParam Long workCenterId,
            @RequestParam Long checkTypeId
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.createWorkCenterCheck(workCenterId, checkTypeId), HttpStatus.OK);
    }

    // 작업장별 점검유형 단일 조회
    @GetMapping("/{work-center-check-id}")
    @ResponseBody()
    @Operation(summary = "작업장별 점검유형 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<WorkCenterCheckResponse> getWorkCenterCheck(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.getWorkCenterCheck(workCenterCheckId), HttpStatus.OK);
    }

    // 작업장별 점검유형 전체 조회/ 검색: 작업장, 점검유형
    @GetMapping
    @ResponseBody
    @Operation(summary = "작업장별 점검유형 전체 조회", description = "검색조건: 작업장, 점검유형")
    public ResponseEntity<List<WorkCenterCheckResponse>> getWorkCenterChecks(
            @RequestParam(required = false) @Parameter(description = "작업장 id") Long workCenterId,
            @RequestParam(required = false) @Parameter(description = "점검유형 id") Long checkTypeId
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.getWorkCenterChecks(workCenterId, checkTypeId), HttpStatus.OK);
    }

    // 작업장별 점검유형 수정
    @PatchMapping("/{work-center-check-id}")
    @ResponseBody
    @Operation(
            summary = "작업장별 점검유형 수정",
            description = "checkTypeId: 작업유형 id / workCenterId: 작업장 id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkCenterCheckResponse> updateWorkCenterCheck(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId,
            @RequestParam Long workCenterId,
            @RequestParam Long checkTypeId
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.updateWorkCenterCheck(workCenterCheckId, workCenterId, checkTypeId), HttpStatus.OK);
    }

    // 작업장별 점검유형 삭제
    @DeleteMapping("/{work-center-check-id}")
    @ResponseBody
    @Operation(summary = "작업장별 점검유형 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteWorkCenterCheck(@PathVariable(value = "work-center-check-id") Long id) throws NotFoundException {
        workCenterCheckService.deleteWorkCenterCheck(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 작업장별 점검유형 세부 생성
    @PostMapping("/{work-center-check-id}/work-center-check-details")
    @ResponseBody
    @Operation(summary = "작업장별 점검유형 세부 생성", description = "lsl, usl 소수점 3자리까지 기입가능")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkCenterCheckDetailResponse> createWorkCenterCheckDetail(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId,
            @RequestBody @Valid WorkCenterCheckDetailRequest workCenterCheckDetailRequest
    ) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(workCenterCheckService.createWorkCenterCheckDetail(workCenterCheckId,workCenterCheckDetailRequest), HttpStatus.OK);
    }

//    // 작업장별 점검유형 세부 단일 조회 api
//    @GetMapping("/{work-center-check-id}/work-center-check-details/{work-center-check-detail-id}")
//    @ResponseBody
//    @Operation(summary = "작업장별 점검유형 세부 단일 조회")
//    public ResponseEntity<WorkCenterCheckDetailResponse> getWorkCenterCheckDetail(
//            @PathVariable(value = "work-center-check-id") Long workCenterCheckId,
//            @PathVariable(value = "work-center-check-detail-id") Long workCenterCheckDetailId
//    ) throws NotFoundException {
//        return new ResponseEntity<>(workCenterCheckService.getWorkCenterCheckDetail(workCenterCheckId, workCenterCheckDetailId), HttpStatus.OK);
//    }

    // 작업장별 점검유형 세부 리스트 조회
    @GetMapping("/{work-center-check-id}/work-center-check-details")
    @ResponseBody
    @Operation(summary = "작업장별 점검유형 세부 리스트 조회", description = "작업장별 점검유형에 해당하는 세부내역 전체조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<List<WorkCenterCheckDetailResponse>> getWorkCenterCheckDetails(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.getWorkCenterCheckDetails(workCenterCheckId), HttpStatus.OK);
    }

    // 작업장별 점검유형 세부 수정
    @PatchMapping("/{work-center-check-id}/work-center-check-details/{work-center-check-detail-id}")
    @ResponseBody
    @Operation(summary = "작업장별 점검유형 세부 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkCenterCheckDetailResponse> updateWorkCenterCheckDetail(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId,
            @PathVariable(value = "work-center-check-detail-id") Long workCenterCheckDetailId,
            @RequestBody @Valid WorkCenterCheckDetailRequest workCenterCheckDetailRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(workCenterCheckService.updateWorkCenterCheckDetail(workCenterCheckId, workCenterCheckDetailId, workCenterCheckDetailRequest), HttpStatus.OK);
    }

    // 작업장별 점검유형 세부 삭제
    @DeleteMapping("/{work-center-check-id}/work-center-check-details/{work-center-check-detail-id}")
    @ResponseBody
    @Operation(summary = "작업장별 점검유형 세부 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteWorkCenterCheckDetail(
            @PathVariable(value = "work-center-check-id") Long workCenterCheckId,
            @PathVariable(value = "work-center-check-detail-id") Long workCenterCheckDetailId
    ) throws NotFoundException {
        workCenterCheckService.deleteWorkCenterCheckDetail(workCenterCheckId, workCenterCheckDetailId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 작업장별 점검유형 페이징 조회/ 검색: 작업장, 점검유형
//    @GetMapping
//    @ResponseBody
//    @Operation(summary = "작업장별 점검유형 페이징 조회", description = "검색조건: 작업장, 점검유형")
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
//    public ResponseEntity<Page<WorkCenterCheckResponse>> getWorkCenterChecks(
//            @RequestParam(required = false) @Parameter(description = "작업장 id") Long workCenterId,
//            @RequestParam(required = false) @Parameter(description = "점검유형 id") Long checkTypeId,
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) throws NotFoundException {
//        return new ResponseEntity<>(workCenterCheckService.getWorkCenterChecks(workCenterId, checkTypeId, pageable), HttpStatus.OK);
//    }
}
