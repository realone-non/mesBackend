package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.CurrencyRequest;
import com.mes.mesBackend.dto.response.CurrencyResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "currency", description = "화폐 API")
@RequestMapping("/currencies")
@RestController
public class CurrencyController {
    @Autowired
    CurrencyService currencyService;

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
            @RequestBody @Valid CurrencyRequest currencyRequest
    ) {
        return new ResponseEntity<>(currencyService.createCurrency(currencyRequest), HttpStatus.OK);
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
    public ResponseEntity<CurrencyResponse> getCurrency(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(currencyService.getCurrency(id), HttpStatus.OK);
    }

    // 화폐 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "화폐 리스트 조회")
    public ResponseEntity<List<CurrencyResponse>> getCurrencies() {
        return new ResponseEntity<>(currencyService.getCurrencies(), HttpStatus.OK);
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
            @RequestBody @Valid CurrencyRequest currencyRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(currencyService.updateCurrency(id, currencyRequest), HttpStatus.OK);
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
            @PathVariable Long id
    ) throws NotFoundException {
        currencyService.deleteCurrency(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
}
