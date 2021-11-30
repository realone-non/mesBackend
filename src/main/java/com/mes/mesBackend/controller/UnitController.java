package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.UnitRequest;
import com.mes.mesBackend.dto.response.UnitResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Tag(name = "unit", description = "단위 API")
@RequestMapping(value = "/units")
@RestController
@RequiredArgsConstructor
public class UnitController {

    @Autowired
    UnitService unitService;

    // 단위 생성
    @PostMapping
    @ResponseBody()
    @Operation(summary = "단위 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<UnitResponse> createUnit(
            @RequestBody @Valid UnitRequest unitRequest
    ) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(unitService.createUnit(unitRequest), HttpStatus.OK);
    }

    // 단위 단일 조회
    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "단위 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<UnitResponse> getUnit(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(unitService.getUnit(id), HttpStatus.OK);
    }

    // 단위 전체 조회
    @GetMapping
    @ResponseBody()
    @Operation(summary = "단위 전체 조회")
    public ResponseEntity<List<UnitResponse>> getUnits() {
        return new ResponseEntity<>(unitService.getUnits(), HttpStatus.OK);
    }

    // 단위 수정
    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "단위 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<UnitResponse> updateUnit(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid UnitRequest unitRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(unitService.updateUnit(id, unitRequest), HttpStatus.OK);
    }

    // 단위 삭제
    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "단위 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) throws NotFoundException {
        unitService.deleteUnit(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 단위 페이징 조회
//    @GetMapping
//    @ResponseBody()
//    @Operation(summary = "단위 페이징 조회")
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
//    public ResponseEntity<Page<UnitResponse>> getUnits(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(unitService.getUnits(pageable), HttpStatus.OK);
//    }
}
