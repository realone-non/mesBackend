package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.HeaderRequest;
import com.mes.mesBackend.dto.request.HolidayRequest;
import com.mes.mesBackend.dto.request.OutsourcingProductionRequestRequest;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.dto.response.HolidayResponse;
import com.mes.mesBackend.dto.response.OutsourcingProductionResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.HeaderService;
import com.mes.mesBackend.service.HolidayService;
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

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "holiday", description = "휴일 등록 API")
@RequestMapping("/holidays")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class HolidayController {

    @Autowired
    HolidayService holidayService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(HeaderController.class);
    private CustomLogger cLogger;

    // 휴일 등록
    @PostMapping
    @ResponseBody
    @Operation(summary = "휴일 등록")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<HolidayResponse> createHoliday(
            @RequestBody @Valid HolidayRequest request,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws BadRequestException {
        HolidayResponse response = holidayService.createHoliday(request);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + response.getId() + " from createHoliday.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 휴일 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "휴일 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<HolidayResponse>> getHolidays(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<HolidayResponse> responseList = holidayService.getHolidays();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    // 휴일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "휴일 조회")
    public ResponseEntity<HolidayResponse> getHoliday(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        HolidayResponse response = holidayService.getHoliday(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemForms.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 휴일 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "휴일 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<HolidayResponse> modifyHoliday(
            @PathVariable Long id,
            @RequestBody @Valid HolidayRequest request,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        HolidayResponse response = holidayService.updateHoliday(id, request);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + response.getId() + " from modifyHoliday.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 휴일 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "휴일 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteHoliday(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        holidayService.deleteHoliday(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteHoliday.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
