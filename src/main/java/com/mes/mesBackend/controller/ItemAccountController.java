package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemAccountRequest;
import com.mes.mesBackend.dto.response.ItemAccountCodeResponse;
import com.mes.mesBackend.dto.response.ItemAccountResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ItemAccountService;
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

// 품목계정
@Tag(name = "item-account", description = "품목계정 API")
@RequestMapping
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class ItemAccountController {
    private final ItemAccountService itemAccountService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(ItemAccountController.class);
    private CustomLogger cLogger;

    // 품목계정 생성
    @PostMapping("/item-accounts")
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
            @RequestBody @Valid ItemAccountRequest itemAccountRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        ItemAccountResponse itemAccount = itemAccountService.createItemAccount(itemAccountRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + itemAccount.getId() + " from createItemAccount.");
        return new ResponseEntity<>(itemAccount, OK);
    }

    // 품목계정 단일 조회
    @GetMapping("/item-accounts/{id}")
    @ResponseBody
    @Operation(summary = "품목계정 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<ItemAccountResponse> getItemAccount(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ItemAccountResponse itemAccount = itemAccountService.getItemAccount(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + itemAccount.getId() + " from getItemAccount.");
        return new ResponseEntity<>(itemAccount, OK);
    }

    // 품목계정 리스트 조회
    @GetMapping("/item-accounts")
    @ResponseBody
    @Operation(summary = "품목계정 리스트 조회")
    public ResponseEntity<List<ItemAccountResponse>> getItemAccounts(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ItemAccountResponse> itemAccounts = itemAccountService.getItemAccounts();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemAccounts.");
        return new ResponseEntity<>(itemAccounts, OK);
    }

    // 품목계정 수정
    @PatchMapping("/item-accounts/{id}")
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
            @RequestBody @Valid ItemAccountRequest itemAccountRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ItemAccountResponse account = itemAccountService.updateItemAccount(id, itemAccountRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + account.getId() + " from updateItemAccount.");
        return new ResponseEntity<>(account, OK);
    }

    // 품목계정 삭제
    @DeleteMapping("/item-accounts/{id}")
    @ResponseBody
    @Operation(summary = "품목계정 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteItemAccount(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        itemAccountService.deleteItemAccount(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteItemAccount.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 품목계정 리스트 조회
    @GetMapping("/item-account-codes")
    @ResponseBody
    @Operation(summary = "품목계정코드 리스트 조회", description = "품목계정 id로 해당하는 코드 검색할 수 있음.")
    public ResponseEntity<List<ItemAccountCodeResponse>> getItemAccountCodes(
            @RequestParam(required = false) @Parameter(description = "품목계정 id") Long itemAccountId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<ItemAccountCodeResponse> itemAccountCodes = itemAccountService.getItemAccountCodes(itemAccountId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemAccountCodes.");
        return new ResponseEntity<>(itemAccountCodes, OK);
    }
}
