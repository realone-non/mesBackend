package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemGroupRequest;
import com.mes.mesBackend.dto.response.ItemGroupResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ItemGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

// 3-2-2. 품목그룹 등록
@Tag(name = "item-group", description = "품목그룹 API")
@RequestMapping("/item-groups")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class ItemGroupController {
    private final ItemGroupService itemGroupService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ItemGroupController.class);
    private CustomLogger cLogger;

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
            @RequestBody @Valid ItemGroupRequest itemGroupRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ItemGroupResponse itemGroup = itemGroupService.createItemGroup(itemGroupRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + itemGroup.getId() + " from createItemGroup.");
        return new ResponseEntity<>(itemGroup, OK);
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
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ItemGroupResponse itemGroup = itemGroupService.getItemGroup(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + itemGroup.getId() + " from getItemGroup.");
        return new ResponseEntity<>(itemGroup, OK);
    }

    // 품목그룹 전체 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "품목그룹 전체 조회")
    public ResponseEntity<List<ItemGroupResponse>> getItemGroups(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ItemGroupResponse> itemGroups = itemGroupService.getItemGroups();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemGroups.");
        return new ResponseEntity<>(itemGroups, OK);
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
            @RequestBody @Valid ItemGroupRequest itemGroupRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        String userCode = logService.getUserCodeFromHeader(tokenHeader);
        ItemGroupResponse itemGroup = itemGroupService.updateItemGroup(id, itemGroupRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(userCode + " is modified the " + itemGroup.getId() + " from updateItemGroup.");
        return new ResponseEntity<>(itemGroup, OK);
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
    public ResponseEntity deleteItemGroup(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        itemGroupService.deleteItemGroup(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteItemGroup.");
        return new ResponseEntity(NO_CONTENT);
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
//        return new ResponseEntity<>(itemGroupService.getItemGroups(pageable), OK);
//    }
}
