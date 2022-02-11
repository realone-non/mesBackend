package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.OutsourcingInputLOTRequest;
import com.mes.mesBackend.dto.request.OutsourcingInputRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
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

    // 외주생산입고 등록
    @PostMapping
    @ResponseBody
    @Operation(summary = "외주생산입고 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<OutsourcingInputResponse> createOutsourcingInput(
            @RequestBody @Valid OutsourcingInputRequest request,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        OutsourcingInputResponse response = outsourcingService.createOutsourcingInput(request);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + response.getId() + " from createOutsourcingInput.");
        return new ResponseEntity<>(response, OK);
    }

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
            @RequestParam(required = false) @Parameter(description = "품번") String itemNo,
            @RequestParam(required = false) @Parameter(description = "품명") String itemName,
            @RequestParam(required = false) @Parameter(description = "시작날짜") LocalDate startDate,
            @RequestParam(required = false) @Parameter(description = "종료날짜") LocalDate endDate,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<OutsourcingInputResponse> responseList = outsourcingService.getOutsourcingInputList(clientId, itemNo, itemName, startDate, endDate);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of clientId: " + clientId +
                "itemNo " + itemNo + "itemName" + itemName + "startDate and EndDate:" + startDate + "," + endDate + " from getOutsourcingInputList.");
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
        OutsourcingInputResponse response = outsourcingService.getOutsourcingInput(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + id + " from getOutsourcingInput.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주 입고정보 수정
    @Operation(summary = "외주 입고정보 수정")
    @PatchMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<OutsourcingInputResponse> modifyOutsourcingInput(
            @PathVariable(value = "id") @Parameter(description = "입고 id") Long id,
            @RequestBody @Valid OutsourcingInputRequest request,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        OutsourcingInputResponse response = outsourcingService.modifyOutsourcingInput(id, request);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + response.getId() + " from modifyOutsourcingInput.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주 입고정보 삭제
    @Operation(summary = "외주 입고정보 삭제")
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity deleteOutsourcingInput(
            @PathVariable(value = "id") @Parameter(description = "입고 id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        outsourcingService.deleteOutsourcingInput(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteOutsourcingInput.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 외주 입고 LOT 정보 등록
    @PostMapping("/{input-id}/lot")
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
            @PathVariable(value = "input-id") @Parameter(description = "입고 id") Long id,
            @RequestBody @Valid OutsourcingInputLOTRequest request,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        OutsourcingInputLOTResponse response = outsourcingService.createOutsourcingInputLOT(id, request);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + response.getId() + " from createOutsourcingInputLOT.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주 입고정보 LOT 리스트조회
    @GetMapping("/{input-id}/lot")
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
            @PathVariable(value = "input-id") @Parameter(description = "입고 ID") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<OutsourcingInputLOTResponse> responseList = outsourcingService.getOutsourcingInputLOTList(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of Id: " + id + " from getOutsourcingInputList.");
        return new ResponseEntity<>(responseList, OK);
    }

    // 외주 입고정보 LOT 조회
    @Operation(summary = "외주 입고정보 LOT 조회")
    @GetMapping("/{input-id}/lot/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<OutsourcingInputLOTResponse> getOutsourcingInputLOT(
            @PathVariable(value = "input-id") @Parameter(description = "입고정보 id") Long inputId,
            @PathVariable(value = "id") @Parameter(description = "LOT id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        OutsourcingInputLOTResponse response = outsourcingService.getOutsourcingInputLOT(inputId, id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + response.getId() + " from getOutsourcingInput.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주 입고정보 LOT 수정
    @Operation(summary = "외주 입고정보 LOT 수정")
    @PatchMapping("/{input-id}/lot/{id}")
    @ResponseBody
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<OutsourcingInputLOTResponse> modifyOutsourcingInputLOT(
            @PathVariable(value = "input-id") @Parameter(description = "입고 id") Long inputId,
            @PathVariable(value = "id") @Parameter(description = "LOT id") Long id,
            @RequestBody @Valid OutsourcingInputLOTRequest request,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        OutsourcingInputLOTResponse response = outsourcingService.modifyOutsourcingInputLOT(inputId, id, request);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + response.getId() + " from modifyOutsourcingInputLOT.");
        return new ResponseEntity<>(response, OK);
    }

    // 외주 입고정보 LOT 삭제
    @Operation(summary = "외주 입고정보 LOT 삭제")
    @DeleteMapping("/{input-id}/lot/{id}")
        @ResponseBody
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "success"),
                        @ApiResponse(responseCode = "404", description = "not found resource"),
                        @ApiResponse(responseCode = "400", description = "bad request")
                }
        )
        public ResponseEntity deleteOutsourcingInputLOT(
                @PathVariable(value = "input-id") @Parameter(description = "입고 id") Long inputId,
                @PathVariable(value = "id") @Parameter(description = "LOT id") Long id,
                @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
            outsourcingService.deleteOutsourcingInputLOT(inputId, id);
            cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
            cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteOutsourcingInputLOT.");
        return new ResponseEntity(NO_CONTENT);
    }
}
