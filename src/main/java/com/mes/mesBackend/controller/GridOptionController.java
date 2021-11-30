package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.GridOptionRequest;
import com.mes.mesBackend.dto.response.GridOptionResponse;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.GridOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "grid-option", description = "그리드 옵션 API")
@RequestMapping("/users/{user-id}/headers")
@RestController
@RequiredArgsConstructor
public class GridOptionController {

    @Autowired
    GridOptionService gridOptionService;

    @PostMapping("/{header-id}/grid-options")
    @ResponseBody
    @Operation(summary = "그리드 옵션 단일 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<GridOptionResponse> createGridOption(
            @PathVariable(value = "header-id") Long headerId,
            @PathVariable(value = "user-id") Long userId,
            @RequestBody GridOptionRequest gridOptionRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(gridOptionService.createGridOption(headerId, gridOptionRequest, userId), HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "해당 유저에 해당하는 그리드 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<List<HeaderResponse>> getHeaderGridOptions(
            @PathVariable(value = "user-id") Long userId,
            @RequestParam(value = "controller-name") String controllerName
    ) throws NotFoundException {
        return new ResponseEntity<>(gridOptionService.getHeaderGridOptions(userId, controllerName), HttpStatus.OK);
    }
}
