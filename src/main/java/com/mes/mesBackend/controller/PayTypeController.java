package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.response.PayTypeResponse;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.PayTypeService;
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
import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.mes.mesBackend.exception.Message.NOT_EMPTY;
import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping(value = "/pay-types")
@Tag(name = "pay-type", description = "결제조건 API")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class PayTypeController {
    private final PayTypeService payTypeService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(PayTypeController.class);
    private CustomLogger cLogger;
    
    @Operation(summary = "결제조건 생성", description = "")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PayTypeResponse> createPayType(
            @RequestParam @Valid @NotBlank(message = NOT_EMPTY) @Parameter(description = "결제조건") String payTypeRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws BadRequestException, NotFoundException {
        PayTypeResponse payType = payTypeService.createPayType(payTypeRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + payType.getId() + " from createPayType.");
        return new ResponseEntity<>(payType, OK);
    }

    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "결제조건 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<PayTypeResponse> getPayType(
            @PathVariable(value = "id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        PayTypeResponse payType = payTypeService.getPayType(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + payType.getId() + " from getPayType.");
        return new ResponseEntity<>(payType, OK);
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "결제조건 전체 조회", description = "")
    public ResponseEntity<List<PayTypeResponse>> getPayTypes(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<PayTypeResponse> payTypes = payTypeService.getPayTypes();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getPayTypes.");
        return new ResponseEntity<>(payTypes, OK);
    }


    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "결제조건 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<PayTypeResponse> updatePayType(
            @PathVariable(value = "id") Long id,
            @RequestParam @Valid @NotBlank(message = NOT_EMPTY) @Parameter(description = "결제조건") String payTypeRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException, BadRequestException {
        PayTypeResponse payType = payTypeService.updatePayType(id, payTypeRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + payType.getId() + " from updatePayType.");
        return new ResponseEntity<>(payType, OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "결제조건 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deletePayType(
            @PathVariable(value = "id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        payTypeService.deletePayType(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deletePayType.");
        return new ResponseEntity(NO_CONTENT);
    }
}
