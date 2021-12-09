package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkDocumentRequest;
import com.mes.mesBackend.dto.response.WorkDocumentResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.WorkDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "work-document", description = "작업표준서 API")
@RequestMapping("/work-documents")
@RestController
@SecurityRequirement(name = "Authorization")
public class WorkDocumentController {
    @Autowired
    WorkDocumentService workDocumentService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(WorkDocumentController.class);
    private CustomLogger cLogger;

    // 작업표준서 생성
    @Operation(summary = "작업표준서 생성")
    @PostMapping
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkDocumentResponse> createWorkDocument(
            @RequestBody @Valid WorkDocumentRequest workDocumentRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkDocumentResponse workDocument = workDocumentService.createWorkDocument(workDocumentRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + workDocument.getId() + " from createWorkDocument.");
        return new ResponseEntity<>(workDocument, HttpStatus.OK);
    }

    // 작업표준서 단일 조회
    @Operation(summary = "작업표준서 단일 조회")
    @GetMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<WorkDocumentResponse> getWorkDocument(
            @PathVariable @Parameter(description = "작업표준서 id") Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkDocumentResponse workDocument = workDocumentService.getWorkDocument(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + workDocument.getId() + " from getWorkDocument.");
        return new ResponseEntity<>(workDocument, HttpStatus.OK);
    }

    // 작업표준서 전체 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
    @Operation(summary = "작업표준서 전체 조회", description = "검색조건: 품목그룹, 품목계정, 품번, 품명")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<WorkDocumentResponse>> getWorkDocuments(
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품목계정 id") Long itemAccountId,
            @RequestParam(required = false) @Parameter(description = "품번") String itemNo,
            @RequestParam(required = false) @Parameter(description = "품명") String itemName,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<WorkDocumentResponse> workDocuments = workDocumentService.getWorkDocuments(itemGroupId, itemAccountId, itemNo, itemName);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of itemGroupId: " + itemGroupId +
                ", itemAccountId: " + itemAccountId + " from getWorkDocuments.");
        return new ResponseEntity<>(workDocuments, HttpStatus.OK);
    }

    // 작업표준서 수정
    @Operation(summary = "작업표준서 수정")
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkDocumentResponse> updateWorkDocument(
            @PathVariable @Parameter(description = "작업표준서 id") Long id,
            @RequestBody @Valid WorkDocumentRequest workDocumentRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        WorkDocumentResponse workDocument = workDocumentService.updateWorkDocument(id, workDocumentRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + workDocument.getId() + " from updateWorkDocument.");
        return new ResponseEntity<>(workDocument, HttpStatus.OK);
    }

    // 작업표준서 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "작업표준서 삭제")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteWorkDocument(
            @PathVariable @Parameter(description = "작업표준서 id") Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        workDocumentService.deleteWorkDocument(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteWorkDocument.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 작업표준서 파일 추가
    @PutMapping(value = "/{id}/files", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "작업표준서 파일 생성")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkDocumentResponse> createFileToWorkDocument(
            @PathVariable Long id,
            @RequestPart MultipartFile file,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException, IOException {
        WorkDocumentResponse fileToWorkDocument = workDocumentService.createFileToWorkDocument(id, file);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + fileToWorkDocument.getId() + " from createFileToWorkDocument.");
        return new ResponseEntity<>(fileToWorkDocument, HttpStatus.OK);
    }

//    @Operation(summary = "작업표준서 페이징 조회", description = "검색조건: 품목그룹, 품목계정, 품번, 품명")
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
//    public ResponseEntity<Page<WorkDocumentResponse>> getWorkDocuments(
//            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
//            @RequestParam(required = false) @Parameter(description = "품목계정 id") Long itemAccountId,
//            @RequestParam(required = false) @Parameter(description = "품번") String itemNo,
//            @RequestParam(required = false) @Parameter(description = "품명") String itemName,
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(workDocumentService.getWorkDocuments(itemGroupId, itemAccountId, itemNo, itemName, pageable), HttpStatus.OK);
//    }
}
