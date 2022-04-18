package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.SubItemRequest;
import com.mes.mesBackend.dto.response.SubItemResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.SubItemService;
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

// 3-2-4. 대체품 등록
@Tag(name = "sub-item", description = "대체품 API")
@RequestMapping("/sub-items")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class SubItemController {
    private final SubItemService subItemService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(SubItemController.class);
    private CustomLogger cLogger;

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
            @RequestBody @Valid SubItemRequest subItemRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        SubItemResponse subItem = subItemService.createSubItem(subItemRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + subItem.getId() + " from createSubItem.");
        return new ResponseEntity<>(subItem, OK);
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
            @PathVariable @Parameter(description = "대체품 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        SubItemResponse subItem = subItemService.getSubItem(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + subItem.getId() + " from getSubItem.");
        return new ResponseEntity<>(subItem, OK);
    }

    // 대체품 전체 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
    @Operation(summary = "대체품 전체 조회", description = "검색조건: 품목그룹, 품목계정, 품번, 품명")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<SubItemResponse>> getSubItems(
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품목계정 id") Long itemAccountId,
            @RequestParam(required = false) @Parameter(description = "품번|품명") String itemNoAndItemName,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<SubItemResponse> subItems = subItemService.getSubItems(itemGroupId, itemAccountId, itemNoAndItemName);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of itemGroupId: " + itemGroupId +
                ", itemAccountId: " + itemAccountId + " from getSubItems.");
        return new ResponseEntity<>(subItems, OK);
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
            @RequestBody @Valid SubItemRequest subItemRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        SubItemResponse subItem = subItemService.updateSubItem(id, subItemRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + subItem.getId() + " from updateSubItem.");
        return new ResponseEntity<>(subItem, OK);
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
    public ResponseEntity deleteSubItem(
            @PathVariable @Parameter(description = "대체품 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        subItemService.deleteSubItem(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteSubItem.");
        return new ResponseEntity(NO_CONTENT);
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
//        return new ResponseEntity<>(subItemService.getSubItems(itemGroupId, itemAccountId, itemNo, itemName, pageable), OK);
//    }
}
