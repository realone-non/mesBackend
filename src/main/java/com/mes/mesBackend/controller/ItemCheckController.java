package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemCheckDetailRequest;
import com.mes.mesBackend.dto.request.ItemCheckRequest;
import com.mes.mesBackend.dto.response.ItemCheckDetailResponse;
import com.mes.mesBackend.dto.response.ItemCheckResponse;
import com.mes.mesBackend.entity.enumeration.TestCategory;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ItemCheckService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(value = "/item-checks")
@Tag(name = "item-check", description = "품목별 검사항목 API")
@RestController
@SecurityRequirement(name = "Authorization")
public class ItemCheckController {
    @Autowired
    ItemCheckService itemCheckService;

    @Operation(summary = "품목별 검사항목 생성", description = "")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemCheckResponse> createItemCheck(
            @RequestBody @Valid ItemCheckRequest itemCheckRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemCheckService.createItemCheck(itemCheckRequest), HttpStatus.OK);
    }

    @GetMapping("/{item-check-id}")
    @ResponseBody()
    @Operation(summary = "품목별 검사항목 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<ItemCheckResponse> getItemCheck(
            @PathVariable(value = "item-check-id") @Parameter(description = "품목별 검사항목 id") Long itemCheckId
    ) throws NotFoundException {
        return new ResponseEntity<>(itemCheckService.getItemCheck(itemCheckId), HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "품목별 검사항목 전체 조회", description = "검색조건: 검사유형, 품목그룹, 품목계정")
    public ResponseEntity<List<ItemCheckResponse>> getItemChecks(
            @RequestParam(required = false) @Parameter(description = "검사유형") TestCategory testCategory,
            @RequestParam(required = false) @Parameter(description = "품목그룹") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품목계정") Long itemAccountId
    ) {
        return new ResponseEntity<>(itemCheckService.getItemChecks(testCategory, itemGroupId, itemAccountId), HttpStatus.OK);
    }

    @PatchMapping("/{item-check-id}")
    @ResponseBody()
    @Operation(summary = "품목별 검사항목 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemCheckResponse> updateItemCheck(
            @PathVariable(value = "item-check-id") @Parameter(description = "품목별 검사항목 id") Long itemCheckId,
            @RequestBody @Valid ItemCheckRequest itemCheckRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemCheckService.updateItemCheck(itemCheckId, itemCheckRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{item-check-id}")
    @ResponseBody()
    @Operation(summary = "품목별 검사항목 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteItemCheck(
            @PathVariable(value = "item-check-id") @Parameter(description = "품목별 검사항목 id")Long itemCheckId
    ) throws NotFoundException {
        itemCheckService.deleteItemCheck(itemCheckId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "품목별 검사항목 디테일 생성", description = "")
    @PostMapping("/{item-check-id}/item-check-details")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ItemCheckDetailResponse> createItemCheckDetails(
            @PathVariable(value = "item-check-id") @Parameter(description = "품목별 검사항목 id")Long itemCheckId,
            @RequestBody @Valid ItemCheckDetailRequest itemCheckDetailRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemCheckService.createItemCheckDetails(itemCheckId, itemCheckDetailRequest), HttpStatus.OK);
    }

    @GetMapping("/{item-check-id}/item-check-details/{item-check-detail-id}")
    @ResponseBody()
    @Operation(summary = "품목별 검사항목 디테일 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<ItemCheckDetailResponse> getItemCheckDetail(
            @PathVariable(value = "item-check-id") @Parameter(description = "품목별 검사항목 id") Long itemCheckId,
            @PathVariable(value = "item-check-detail-id") @Parameter(description = "품목별 검사항목 디테일 id") Long itemCheckDetailId
    ) throws NotFoundException {
        return new ResponseEntity<>(itemCheckService.getItemCheckDetail(itemCheckId, itemCheckDetailId), HttpStatus.OK);
    }

    @GetMapping("/{item-check-id}/item-check-details")
    @ResponseBody
    @Operation(summary = "품목별 검사항목 디테일 리스트 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<ItemCheckDetailResponse>> getItemCheckDetails(
            @PathVariable(value = "item-check-id") @Parameter(description = "품목별 검사항목 id") Long itemCheckId
    ) throws NotFoundException {
        return new ResponseEntity<>(itemCheckService.getItemCheckDetails(itemCheckId), HttpStatus.OK);
    }

    @PatchMapping("/{item-check-id}/item-check-details/{item-check-detail-id}")
    @ResponseBody()
    @Operation(summary = "품목별 검사항목 디테일 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemCheckDetailResponse> updateItemCheckDetail(
            @PathVariable(value = "item-check-id") @Parameter(description = "품목별 검사항목 id") Long itemCheckId,
            @PathVariable(value = "item-check-detail-id") @Parameter(description = "품목별 검사항목 디테일 id") Long itemCheckDetailId,
            @RequestBody @Valid ItemCheckDetailRequest itemCheckDetailRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemCheckService.updateItemCheckDetail(itemCheckId, itemCheckDetailId, itemCheckDetailRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{item-check-id}/item-check-details/{item-check-detail-id}")
    @ResponseBody()
    @Operation(summary = "품목별 검사항목 디테일 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteItemCheckDetail(
            @PathVariable(value = "item-check-id") @Parameter(description = "품목별 검사항목 id")Long itemCheckId,
            @PathVariable(value = "item-check-detail-id") @Parameter(description = "품목별 검사항목 디테일 id")Long itemCheckDetailId
    ) throws NotFoundException {
        itemCheckService.deleteItemCheckDetail(itemCheckId, itemCheckDetailId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @GetMapping
//    @ResponseBody
//    @Operation(summary = "품목별 검사항목 페이징 조회", description = "검색조건: 검사유형, 품목그룹, 품목계정")
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
//    public ResponseEntity<Page<ItemCheckResponse>> getItemChecks(
//            @RequestParam(required = false) @Parameter(description = "검사유형") TestCategory testCategory,
//            @RequestParam(required = false) @Parameter(description = "품목그룹") Long itemGroupId,
//            @RequestParam(required = false) @Parameter(description = "품목계정") Long itemAccountId,
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(itemCheckService.getItemChecks(testCategory, itemGroupId, itemAccountId, pageable), HttpStatus.OK);
//    }
}
