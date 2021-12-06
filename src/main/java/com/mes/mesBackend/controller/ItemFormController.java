package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemFormRequest;
import com.mes.mesBackend.dto.response.ItemFormResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.ItemFormService;
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
import java.util.List;

// 품목형태
@Tag(name = "item-form", description = "품목형태 API")
@RequestMapping(value = "/item-forms")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class ItemFormController {

    @Autowired
    ItemFormService itemFormService;

    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(ItemFormController.class);
    private CustomLogger cLogger;

    // 품목형태 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "품목형태 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemFormResponse> createItemForm(
            @RequestBody @Valid ItemFormRequest itemFormRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        ItemFormResponse itemForm = itemFormService.createItemForm(itemFormRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + "is created the " + itemForm.getId() + " from createItemForm.");
        return new ResponseEntity<>(itemForm, HttpStatus.OK);
    }

    // 품목형태 단일 조회
    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "품목형태 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<ItemFormResponse> getItemForm(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ItemFormResponse itemForm = itemFormService.getItemForm(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the " + itemForm.getId() + " from getItemForm.");
        return new ResponseEntity<>(itemForm, HttpStatus.OK);
    }

    // 품목형태 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "품목형태 리스트 조회")
    public ResponseEntity<List<ItemFormResponse>> getItemForms(
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<ItemFormResponse> itemForms = itemFormService.getItemForms();
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getItemForms.");
        return new ResponseEntity<>(itemForms, HttpStatus.OK);
    }

    // 품목형태 수정
    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "품목형태 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<ItemFormResponse> updateItemForm(
            @PathVariable Long id,
            @RequestBody @Valid ItemFormRequest itemFormRequest,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        ItemFormResponse itemForm = itemFormService.updateItemForm(id, itemFormRequest);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + itemForm.getId() + " from updateItemForm.");
        return new ResponseEntity<>(itemForm, HttpStatus.OK);
    }

    // 품목형태 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "품목형태 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteItemForm(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        itemFormService.deleteItemForm(id);
        cLogger = new MongoLogger(logger, "mongoTemplate");
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteItemForm.");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
