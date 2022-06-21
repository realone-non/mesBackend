package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.OutsourcingInputLOTRequest;
import com.mes.mesBackend.dto.response.OutsourcingInputLOTResponse;
import com.mes.mesBackend.dto.response.OutsourcingInputResponse;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;


// 10-1 외주 입고 등록
@Tag(name = "outsourcing-input", description = "외주입고 API")
@RequestMapping("/outsourcing-input")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class OutsourcingInputController {
    private final LogService logService;
    private final OutsourcingService outsourcingService;
    private final Logger logger = LoggerFactory.getLogger(OutsourcingInputController.class);
    private CustomLogger cLogger;

    // 외주 입고정보 리스트조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "외주 입고정보 리스트조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<OutsourcingInputResponse>> getOutsourcingInputList(
            @RequestParam(required = false) @Parameter(description = "외주사 ID") Long clientId,
            @RequestParam(required = false) @Parameter(description = "품번|품명") String itemNoAndItemName,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "시작날짜") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE) @Parameter(description = "종료날짜") LocalDate endDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<OutsourcingInputResponse> responseList = outsourcingService.getOutsourcingInputList(clientId, itemNoAndItemName, startDate, endDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of clientId: " + clientId +
                "itemNoAndItemName:  " + itemNoAndItemName + "startDate and EndDate:" + startDate + "," + endDate + " from getOutsourcingInputList.");
        return new ResponseEntity<>(responseList, OK);
    }

    // 외주 입고정보 조회
    @Operation(summary = "외주 입고정보 조회")
    @GetMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<OutsourcingInputResponse> getOutsourcingInput(
            @PathVariable(value = "id") @Parameter(description = "입고정보 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        OutsourcingInputResponse response = outsourcingService.getOutsourcingInputResponseOrThrow(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + id + " from getOutsourcingInput.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주 입고 LOT 정보 등록
    @PostMapping("/{request-id}/lot")
    @ResponseBody
    @Operation(summary = "외주 입고 LOT 정보 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<OutsourcingInputLOTResponse> createOutsourcingInputLOT(
            @PathVariable(value = "request-id") @Parameter(description = "외주생산의뢰 id") Long requestid,
            @RequestBody @Valid OutsourcingInputLOTRequest request,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        OutsourcingInputLOTResponse response = outsourcingService.createOutsourcingInputLOT(requestid, request);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + response.getId() + " from createOutsourcingInputLOT.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주 입고정보 LOT 리스트조회
    @GetMapping("/{request-id}/lot")
    @ResponseBody
    @Operation(summary = "외주 입고정보 리스트조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<OutsourcingInputLOTResponse>> getOutsourcingInputLOTList(
            @PathVariable(value = "request-id") @Parameter(description = "외주생산의뢰 ID") Long requestid,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<OutsourcingInputLOTResponse> responseList = outsourcingService.getOutsourcingInputLOTList(requestid);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of Id: " + requestid + " from getOutsourcingInputList.");
        return new ResponseEntity<>(responseList, OK);
    }

    // 외주 입고정보 LOT 조회
    @Operation(summary = "외주 입고정보 LOT 조회")
    @GetMapping("/{request-id}/lot/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<OutsourcingInputLOTResponse> getOutsourcingInputLOT(
            @PathVariable(value = "request-id") @Parameter(description = "외주생산의뢰 id") Long requestid,
            @PathVariable(value = "id") @Parameter(description = "외주입고 id") Long inputId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        OutsourcingInputLOTResponse response = outsourcingService.getOutsourcingInputLOTResponseOrThrow(requestid, inputId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + response.getId() + " from getOutsourcingInput.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주 입고정보 LOT 수정
    @Operation(summary = "외주 입고정보 LOT 수정")
    @PatchMapping("/{request-id}/lot/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<OutsourcingInputLOTResponse> modifyOutsourcingInputLOT(
            @PathVariable(value = "request-id") @Parameter(description = "외주생산의뢰 id") Long requestid,
            @PathVariable(value = "id") @Parameter(description = "외주입고 id") Long inputId,
            @RequestBody @Valid OutsourcingInputLOTRequest request,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        OutsourcingInputLOTResponse response = outsourcingService.modifyOutsourcingInputLOT(requestid, inputId, request);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + response.getId() + " from modifyOutsourcingInputLOT.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주 입고정보 LOT 삭제
    @Operation(summary = "외주 입고정보 LOT 삭제")
    @DeleteMapping("/{request-id}/lot/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity deleteOutsourcingInputLOT(
            @PathVariable(value = "request-id") @Parameter(description = "외주생산의뢰 id") Long requestid,
            @PathVariable(value = "id") @Parameter(description = "외주입고 id") Long inputId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        outsourcingService.deleteOutsourcingInputLOT(requestid, inputId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + inputId + " from deleteOutsourcingInputLOT.");
        return new ResponseEntity(NO_CONTENT);
    }
}
