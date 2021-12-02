package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemAccountRequest;
import com.mes.mesBackend.dto.response.ItemAccountResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ItemAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 품목계정
@Tag(name = "item-account", description = "품목계정 API")
@RequestMapping(value = "/item-accounts")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class ItemAccountController {
    @Autowired
    ItemAccountService itemAccountService;

    // 품목계정 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "품목계정 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemAccountResponse> createItemAccount(
            @RequestBody @Valid ItemAccountRequest itemAccountRequest
    ) {
        return new ResponseEntity<>(itemAccountService.createItemAccount(itemAccountRequest), HttpStatus.OK);
    }

    // 품목계정 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "품목계정 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<ItemAccountResponse> getItemAccount(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(itemAccountService.getItemAccount(id), HttpStatus.OK);
    }

    // 품목계정 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "품목계정 리스트 조회")
    public ResponseEntity<List<ItemAccountResponse>> getItemAccounts() {
        return new ResponseEntity<>(itemAccountService.getItemAccounts(), HttpStatus.OK);
    }

    // 품목계정 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "품목계정 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemAccountResponse> updateItemAccount(
            @PathVariable Long id,
            @RequestBody @Valid ItemAccountRequest itemAccountRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemAccountService.updateItemAccount(id, itemAccountRequest), HttpStatus.OK);
    }

    // 품목계정 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "품목계정 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteItemAccount(@PathVariable Long id) throws NotFoundException {
        itemAccountService.deleteItemAccount(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
