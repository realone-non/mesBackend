package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CurrencyRequest;
import com.mes.mesBackend.dto.response.CurrencyResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.CurrencyService;
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

@Tag(name = "currency", description = "화폐 API")
@RequestMapping("/currencies")
@RestController
@SecurityRequirement(name = AUTHORIZATION)
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(CurrencyController.class);
    private CustomLogger cLogger;

    // 화폐 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "화폐 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<CurrencyResponse> createCurrency(
            @RequestBody @Valid CurrencyRequest currencyRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        CurrencyResponse currency = currencyService.createCurrency(currencyRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + currency.getId() + " from createCurrency.");
        return new ResponseEntity<>(currency, OK);
    }

    // 화폐 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "화폐 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<CurrencyResponse> getCurrency(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        CurrencyResponse currency = currencyService.getCurrency(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + currency.getId() + " from getCurrency.");
        return new ResponseEntity<>(currency, OK);
    }

    // 화폐 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "화폐 리스트 조회")
    public ResponseEntity<List<CurrencyResponse>> getCurrencies(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<CurrencyResponse> currencies = currencyService.getCurrencies();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getCurrencies.");
        return new ResponseEntity<>(currencies, OK);
    }

    // 화폐 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "화폐 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<CurrencyResponse> updateCurrency(
            @PathVariable Long id,
            @RequestBody @Valid CurrencyRequest currencyRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        CurrencyResponse currency = currencyService.updateCurrency(id, currencyRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + currency.getId() + " from updateCurrency.");
        return new ResponseEntity<>(currency, OK);
    }

    // 화폐 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "화폐 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteCurrency(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        currencyService.deleteCurrency(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteCurrency.");
        return new ResponseEntity(NO_CONTENT);
    }
}
