package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkDocumentRequest;
import com.mes.mesBackend.dto.response.WorkDocumentResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WorkDocumentService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "work-document", description = "작업표준서 API")
@RequestMapping("/work-documents")
@RestController
public class WorkDocumentController {
    @Autowired
    WorkDocumentService workDocumentService;

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
            @RequestBody @Valid WorkDocumentRequest workDocumentRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(workDocumentService.createWorkDocument(workDocumentRequest), HttpStatus.OK);
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
            @PathVariable @Parameter(description = "작업표준서 id") Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(workDocumentService.getWorkDocument(id), HttpStatus.OK);
    }

    // 작업표준서 전체 조회 검색조건: 품목그룹, 품목계정, 품번, 품명
    @Operation(summary = "작업표준서 전체 조회", description = "검색조건: 품목그룹, 품목계정, 품번, 품명")
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<WorkDocumentResponse>> getWorkDocuments(
            @RequestParam(required = false) @Parameter(description = "품목그룹 id") Long itemGroupId,
            @RequestParam(required = false) @Parameter(description = "품목계정 id") Long itemAccountId,
            @RequestParam(required = false) @Parameter(description = "품번") String itemNo,
            @RequestParam(required = false) @Parameter(description = "품명") String itemName
    ) {
        return new ResponseEntity<>(workDocumentService.getWorkDocuments(itemGroupId, itemAccountId, itemNo, itemName), HttpStatus.OK);
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
            @RequestBody @Valid WorkDocumentRequest workDocumentRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(workDocumentService.updateWorkDocument(id, workDocumentRequest), HttpStatus.OK);
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
    public ResponseEntity deleteWorkDocument(@PathVariable @Parameter(description = "작업표준서 id") Long id) throws NotFoundException {
        workDocumentService.deleteWorkDocument(id);
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
            @RequestPart MultipartFile file
    ) throws NotFoundException, BadRequestException, IOException {
        return new ResponseEntity<>(workDocumentService.createFileToWorkDocument(id, file), HttpStatus.OK);
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
