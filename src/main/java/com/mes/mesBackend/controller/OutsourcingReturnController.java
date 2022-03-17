package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.OutsourcingReturnRequest;
import com.mes.mesBackend.dto.response.OutsourcingReturnResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.OutsourcingService;
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
import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "outsourcing-return", description = "외주반품관리 API")
@RequestMapping(value = "/outsourcing-returns")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class OutsourcingReturnController {
    private final OutsourcingService outsourcingService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ItemFormController.class);
    private CustomLogger cLogger;

    // 외주반품 등록
    @PostMapping
    @ResponseBody
    @Operation(summary = "외주반품 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<OutsourcingReturnResponse> createOutsourcingReturn(
            @RequestBody @Valid OutsourcingReturnRequest request,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        OutsourcingReturnResponse response = outsourcingService.createOutsourcingReturn(request);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + response.getId() + " from createOutsourcingReturnController.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주반품 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "외주반품 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<OutsourcingReturnResponse>> getOutsourcingReturns(
            @RequestParam(required = false) @Parameter(description = "외주사 ID") Long clientId,
            @RequestParam(required = false) @Parameter(description = "품번") String itemNo,
            @RequestParam(required = false) @Parameter(description = "품명") String itemName,
            @RequestParam(required = false) @Parameter(description = "시작날짜") LocalDate startDate,
            @RequestParam(required = false) @Parameter(description = "종료날짜") LocalDate endDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<OutsourcingReturnResponse> responseList = outsourcingService.getOutsourcingReturnList(clientId, itemNo, itemName, startDate, endDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        return new ResponseEntity<>(responseList, OK);
    }

    // 외주반품 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "외주반품 단일 조회")
    public ResponseEntity<OutsourcingReturnResponse> getOutsourcingReturn(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        OutsourcingReturnResponse response = outsourcingService.getOutsourcingReturn(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemForms.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주 반품 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "외주 반품 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<OutsourcingReturnResponse> modifyOutsourcingReturn(
            @PathVariable Long id,
            @RequestBody @Valid OutsourcingReturnRequest request,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        OutsourcingReturnResponse response = outsourcingService.modifyOutsourcingReturn(id, request);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + response.getId() + " from modifyOutsourcingProduction.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주 반품 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "외주반품 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteOutsourcingReturn(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        outsourcingService.deleteOutsourcingReturn(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteOutsourcingProduction.");
        return new ResponseEntity(NO_CONTENT);
    }
}
