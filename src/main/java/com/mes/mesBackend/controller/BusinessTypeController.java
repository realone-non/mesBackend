package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.BusinessTypeRequest;
import com.mes.mesBackend.dto.response.BusinessTypeResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.BusinessTypeService;
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


@RequestMapping(value = "/business-types")
@Tag(name = "business-type", description = "업태 API")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class BusinessTypeController {
    private final BusinessTypeService businessTypeService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(BusinessTypeController.class);
    private CustomLogger cLogger;

    @Operation(summary = "업태생성", description = "")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<BusinessTypeResponse> createBusinessType(
            @RequestBody @Valid BusinessTypeRequest businessTypeRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        BusinessTypeResponse businessType = businessTypeService.createBusinessType(businessTypeRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + businessType.getId() + " from createBusinessType.");
        return new ResponseEntity<>(businessType, OK);
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
            @PathVariable(value = "id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        BusinessTypeResponse businessType = businessTypeService.getBusinessType(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + businessType.getId() + " from getBusinessType.");
        return new ResponseEntity<>(businessType, OK);
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "업태 전체 조회", description = "")
    public ResponseEntity<List<BusinessTypeResponse>> getBusinessTypes(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<BusinessTypeResponse> businessTypes = businessTypeService.getBusinessTypes();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getBusinessTypes.");
        return new ResponseEntity<>(businessTypes, OK);
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
            @RequestBody @Valid BusinessTypeRequest businessTypeRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        BusinessTypeResponse businessType = businessTypeService.updateBusinessType(id, businessTypeRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + businessType.getId() + " from updateBusinessType.");
        return new ResponseEntity<>(businessType, OK);
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
    public ResponseEntity<Void> deleteBusinessType(
            @PathVariable(value = "id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String header
    ) throws NotFoundException {
        businessTypeService.deleteBusinessType(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(header) + " is deleted the " + id + " from deleteBusinessType.");
        return new ResponseEntity(NO_CONTENT);
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