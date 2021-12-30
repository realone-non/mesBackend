package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemFileRequest;
import com.mes.mesBackend.dto.request.ItemRequest;
import com.mes.mesBackend.dto.response.ItemFileResponse;
import com.mes.mesBackend.dto.response.ItemResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

// 3-2-1. 품목 등록
@Tag(name = "item", description = "품목 API")
@RequestMapping(value = "/items")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class ItemController {

    @Autowired
    ItemService itemService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(ItemController.class);
    private CustomLogger cLogger;

    // 품목 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "품목 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemResponse> createItem(
            @Valid @RequestBody ItemRequest itemRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        ItemResponse item = itemService.createItem(itemRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + item.getId() + " from createItem.");
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    // 품목 단일 조회
    @GetMapping("/{item-id}")
    @ResponseBody()
    @Operation(summary = "품목 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<ItemResponse> getItem(
            @PathVariable(value = "item-id") Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ItemResponse item = itemService.getItem(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + item.getId() + " from getItem.");
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    // 품목 전체 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "품목 전체 조회", description = "검색조건: 품목그룹, 품목계정, 품번, 품명, 검색어")
    public ResponseEntity<List<ItemResponse>> getItems(
            @RequestParam(required = false) @Parameter(description = "품목 그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품목 계정 id") Long itemAccountId,
            @RequestParam(required = false) @Parameter(description = "품번") String itemNo,
            @RequestParam(required = false) @Parameter(description = "품명") String itemName,
            @RequestParam(required = false) @Parameter(description = "검색어") String searchWord,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ItemResponse> items = itemService.getItems(itemGroupId, itemAccountId, itemNo, itemName, searchWord);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of itemGroupId: " + itemGroupId
                + ", itemAccountId: " + itemGroupId + " from getItems.");
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // 품목 수정
    @PatchMapping("/{item-id}")
    @ResponseBody()
    @Operation(summary = "품목 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable(value = "item-id") Long id,
            @RequestBody @Valid ItemRequest itemRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        ItemResponse itemResponse = itemService.updateItem(id, itemRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + itemResponse.getId() + " from updateItem.");
        return new ResponseEntity<>(itemResponse, HttpStatus.OK);
    }

    // 품목 삭제
    @DeleteMapping("/{item-id}")
    @ResponseBody()
    @Operation(summary = "품목 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteItem(
            @PathVariable(value = "item-id") Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        itemService.deleteItem(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteItem.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 파일 정보 생성
    @PostMapping("/{item-id}/item-files")
    @ResponseBody
    @Operation(summary = "품목 파일 정보 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemFileResponse> createItemFileInfo(
            @PathVariable(value = "item-id") Long itemId,
            @RequestBody @Valid ItemFileRequest itemFileRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ItemFileResponse itemFileInfo = itemService.createItemFileInfo(itemId, itemFileRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + itemFileInfo.getId() + " from createItemFileInfo.");
        return new ResponseEntity<>(itemFileInfo, HttpStatus.OK);
    }

    // 파일 생성
    @PutMapping(value = "/{item-id}/item-files/{item-file-id}", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "품목 파일 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemFileResponse> createItemFile(
            @PathVariable(value = "item-id") Long itemId,
            @PathVariable(value = "item-file-id") Long itemFileId,
            @RequestPart MultipartFile file,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, IOException, BadRequestException {
        ItemFileResponse fileResponse = itemService.createFile(itemId, itemFileId, file);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + fileResponse.getId() + " from createItemFile.");
        return new ResponseEntity<>(fileResponse, HttpStatus.OK);
    }

    // 파일 리스트 조회
    @GetMapping("/{item-id}/item-files")
    @ResponseBody
    @Operation(summary = "품목 파일 리스트 조회")
    public ResponseEntity<List<ItemFileResponse>> getItemFiles(
            @PathVariable(value = "item-id") Long itemId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ItemFileResponse> itemFiles = itemService.getItemFiles(itemId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemFiles.");
        return new ResponseEntity<>(itemFiles, HttpStatus.OK);
    }

    // 품목 파일 정보 수정
    @PatchMapping("/{item-id}/item-files/{item-file-id}"
    )
    @ResponseBody
    @Operation(summary = "품목 파일 정보 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemFileResponse> updateItemFileInfo(
            @PathVariable(value = "item-id") Long itemId,
            @PathVariable(value = "item-file-id") Long itemFileId,
            @RequestBody @Valid ItemFileRequest itemFileRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ItemFileResponse itemFileResponse = itemService.updateItemFileInfo(itemId, itemFileId, itemFileRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + itemFileResponse.getId() + " from updateItemFileInfo.");
        return new ResponseEntity<>(itemFileResponse, HttpStatus.OK);
    }

    // 파일 삭제
    @DeleteMapping("/{item-id}/item-files/{item-file-id}")
    @ResponseBody
    @Operation(summary = "품목 파일 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteItemFile(
            @PathVariable(value = "item-id") Long itemId,
            @PathVariable(value = "item-file-id") Long itemFileId,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        itemService.deleteItemFile(itemId, itemFileId);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + itemFileId + " from deleteItemFile.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 품목 페이징 조회
//    @GetMapping
//    @ResponseBody
//    @Operation(summary = "품목 페이징 조회", description = "검색조건: 품목그룹, 품목계정, 품번, 품명, 검색어")
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
//    public ResponseEntity<Page<ItemResponse>> getItems(
//            @RequestParam(required = false) @Parameter(description = "품목 그룹 id") Long itemGroupId,
//            @RequestParam(required = false) @Parameter(description = "품목 계정 id") Long itemAccountId,
//            @RequestParam(required = false) @Parameter(description = "품번") String itemNo,
//            @RequestParam(required = false) @Parameter(description = "품명") String itemName,
//            @RequestParam(required = false) @Parameter(description = "검색어") String searchWord,
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(itemService.getItems(itemGroupId, itemAccountId, itemNo, itemName, searchWord, pageable), HttpStatus.OK);
//    }
}
