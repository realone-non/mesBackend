package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemGroupRequest;
import com.mes.mesBackend.dto.response.ItemGroupResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ItemGroupService;
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

// 3-2-2. 품목그룹 등록
@Tag(name = "item-group", description = "품목그룹 API")
@RequestMapping("/item-groups")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class ItemGroupController {
    @Autowired
    ItemGroupService itemGroupService;

    // 품목그룹 생성
    @PostMapping
    @ResponseBody()
    @Operation(summary = "품목그룹 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemGroupResponse> createItemGroup(
            @RequestBody @Valid ItemGroupRequest itemGroupRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemGroupService.createItemGroup(itemGroupRequest), HttpStatus.OK);
    }

    // 품목그룹 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "품목그룹 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<ItemGroupResponse> getItemGroup(
            @PathVariable Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(itemGroupService.getItemGroup(id), HttpStatus.OK);
    }

    // 품목그룹 전체 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "품목그룹 전체 조회")
    public ResponseEntity<List<ItemGroupResponse>> getItemGroups() {
        return new ResponseEntity<>(itemGroupService.getItemGroups(), HttpStatus.OK);
    }

    // 품목그룹 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "품목그룹 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemGroupResponse> updateItemGroup(
            @PathVariable Long id,
            @RequestBody @Valid ItemGroupRequest itemGroupRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemGroupService.updateItemGroup(id, itemGroupRequest), HttpStatus.OK);
    }

    // 품목그룹 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "품목그룹 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteItemGroup(@PathVariable Long id) throws NotFoundException {
        itemGroupService.deleteItemGroup(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    // 품목그룹 페이징 조회
//    @GetMapping
//    @ResponseBody()
//    @Operation(summary = "품목그룹 페이징 조회")
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
//    public ResponseEntity<Page<ItemGroupResponse>> getItemGroups(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(itemGroupService.getItemGroups(pageable), HttpStatus.OK);
//    }
}
