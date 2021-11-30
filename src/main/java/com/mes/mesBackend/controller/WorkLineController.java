package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.WorkLineRequest;
import com.mes.mesBackend.dto.response.WorkLineResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.WorkLineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 3-3-3. 작업라인 등록
@Tag(name = "work-line", description = "작업라인 API")
@RequestMapping("/work-lines")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class WorkLineController {
    @Autowired
    WorkLineService workLineService;

    // 작업라인 생성
    @PostMapping
    @ResponseBody()
    @Operation(summary = "작업라인 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkLineResponse> createWorkLine(
            @RequestBody @Valid WorkLineRequest workLineRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(workLineService.createWorkLine(workLineRequest), HttpStatus.OK);
    }

    // 작업라인 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업라인 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<WorkLineResponse> getWorkLine(
            @PathVariable Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(workLineService.getWorkLine(id), HttpStatus.OK);
    }

    // 작업라인 전체 조회
    @GetMapping
    @ResponseBody()
    @Operation(summary = "작업라인 전체 조회")
    public ResponseEntity<List<WorkLineResponse>> getWorkLines() {
        return new ResponseEntity<>(workLineService.getWorkLines(), HttpStatus.OK);
    }

    // 작업라인 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업라인 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<WorkLineResponse> updateWorkLine(
            @PathVariable Long id,
            @RequestBody @Valid WorkLineRequest workLineRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(workLineService.updateWorkLine(id, workLineRequest), HttpStatus.OK);
    }

    // 작업라인 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "작업라인 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteWorkLine(
            @PathVariable Long id
    ) throws NotFoundException {
        workLineService.deleteWorkLine(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 작업라인 페이징 조회
//    @GetMapping
//    @ResponseBody()
//    @Operation(summary = "작업라인 페이징 조회")
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
//    public ResponseEntity<Page<WorkLineResponse>> getWorkLines(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(workLineService.getWorkLines(pageable), HttpStatus.OK);
//    }
}
