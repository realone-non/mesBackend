package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.SubItemRequest;
import com.mes.mesBackend.dto.response.SubItemResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.SubItemService;
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

// 3-2-4. 대체품 등록
@Tag(name = "sub-item", description = "대체품 API")
@RequestMapping("/sub-items")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class SubItemController {

    @Autowired
    SubItemService subItemService;

    // 대체품 생성
    @Operation(summary = "대체품 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<SubItemResponse> createSubItem(
            @RequestBody @Valid SubItemRequest subItemRequest
    ) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(subItemService.createSubItem(subItemRequest), HttpStatus.OK);
    }

    // 대체품 단일 조회
    @Operation(summary = "대체품 단일 조회")
    @GetMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<SubItemResponse> getSubItem(
            @PathVariable @Parameter(description = "대체품 id") Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(subItemService.getSubItem(id), HttpStatus.OK);
    }

    // 대체품 전체 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
    @Operation(summary = "대체품 전체 조회", description = "검색조건: 품목그룹, 품목계정, 품번, 품명")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<SubItemResponse>> getSubItems(
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품목계정 id") Long itemAccountId,
            @RequestParam(required = false) @Parameter(description = "품번") String itemNo,
            @RequestParam(required = false) @Parameter(description = "품명") String itemName
    ) {
        return new ResponseEntity<>(subItemService.getSubItems(itemGroupId, itemAccountId, itemNo, itemName), HttpStatus.OK);
    }

    // 대체품 수정
    @Operation(summary = "대체품 수정")
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<SubItemResponse> updateSubItem(
            @PathVariable @Parameter(description = "대체품 id") Long id,
            @RequestBody @Valid SubItemRequest subItemRequest
    ) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(subItemService.updateSubItem(id, subItemRequest), HttpStatus.OK);
    }

    // 대체품 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "대체품 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteSubItem(@PathVariable @Parameter(description = "대체품 id") Long id) throws NotFoundException {
        subItemService.deleteSubItem(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 대체품 페이징 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
//    @Operation(summary = "대체품 페이징 조회", description = "검색조건: 품목그룹, 품목계정, 품번, 품명")
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
//    public ResponseEntity<Page<SubItemResponse>> getSubItems(
//            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
//            @RequestParam(required = false) @Parameter(description = "품목계정 id") Long itemAccountId,
//            @RequestParam(required = false) @Parameter(description = "품번") String itemNo,
//            @RequestParam(required = false) @Parameter(description = "품명") String itemName,
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(subItemService.getSubItems(itemGroupId, itemAccountId, itemNo, itemName, pageable), HttpStatus.OK);
//    }
}
