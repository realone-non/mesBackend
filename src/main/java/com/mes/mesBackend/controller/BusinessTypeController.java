package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.BusinessTypeRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.BusinessTypeService;
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


@RequestMapping(value = "/business-types")
@Tag(name = "business-type", description = "업태 API")
@RestController
@SecurityRequirement(name = "Authorization")
public class BusinessTypeController {

    @Autowired
    BusinessTypeService businessTypeService;

    @Operation(summary = "업태생성", description = "")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<BusinessTypeResponse> createBusinessType(
            @RequestBody @Valid BusinessTypeRequest businessTypeRequest
    ) {
        return new ResponseEntity<>(businessTypeService.createBusinessType(businessTypeRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "업태 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<BusinessTypeResponse> getBusinessType(
            @PathVariable(value = "id") Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(businessTypeService.getBusinessType(id), HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "업태 전체 조회", description = "")
    public ResponseEntity<List<BusinessTypeResponse>> getBusinessTypes() {
        return new ResponseEntity<>(businessTypeService.getBusinessTypes(), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "업태 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<BusinessTypeResponse> updateBusinessType(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid BusinessTypeRequest businessTypeRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(businessTypeService.updateBusinessType(id, businessTypeRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteBusinessType(@PathVariable(value = "id") Long id) throws NotFoundException {
        businessTypeService.deleteBusinessType(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

//    @GetMapping
//    @ResponseBody
//    @Operation(summary = "업태 페이징 조회", description = "")
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
//    public ResponseEntity<Page<BusinessTypeResponse>> getBusinessTypes(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(businessTypeService.getBusinessTypes(pageable), HttpStatus.OK);
//    }
}