package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.ItemFormRequest;
import com.mes.mesBackend.dto.response.ItemFormResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.ItemFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
public class ItemFormController {

    @Autowired
    ItemFormService itemFormService;

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
            @RequestBody @Valid ItemFormRequest itemFormRequest
    ) {
        return new ResponseEntity<>(itemFormService.createItemForm(itemFormRequest), HttpStatus.OK);
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
    public ResponseEntity<ItemFormResponse> getItemForm(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(itemFormService.getItemForm(id), HttpStatus.OK);
    }

    // 품목형태 리스트 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "품목형태 리스트 조회")
    public ResponseEntity<List<ItemFormResponse>> getItemForms() {
        return new ResponseEntity<>(itemFormService.getItemForms(), HttpStatus.OK);
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
            @RequestBody @Valid ItemFormRequest itemFormRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(itemFormService.updateItemForm(id, itemFormRequest), HttpStatus.OK);
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
            @PathVariable Long id
    ) throws NotFoundException {
        itemFormService.deleteItemForm(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
