package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.GridOptionRequest;
import com.mes.mesBackend.dto.response.GridOptionResponse;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.GridOptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{user-id}/headers")
@Api(tags = "grid-option")
@RequiredArgsConstructor
public class GridOptionController {

    @Autowired
    GridOptionService gridOptionService;

    @PostMapping("/{header-id}/grid-options")
    @ResponseBody
    @ApiOperation(value = "그리드 옵션 단일 생성")
    public ResponseEntity<GridOptionResponse> createGridOption(
            @PathVariable(value = "header-id") Long headerId,
            @PathVariable(value = "user-id") Long userId,
            @RequestBody GridOptionRequest gridOptionRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(gridOptionService.createGridOption(headerId, gridOptionRequest, userId), HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "해당 유저에 해당하는 그리드 조회")
    public ResponseEntity<List<HeaderResponse>> getHeaderGridOptions(
            @PathVariable(value = "user-id") Long userId,
            @RequestParam(value = "controller-name") String controllerName
    ) throws NotFoundException {
        return new ResponseEntity<>(gridOptionService.getHeaderGridOptions(userId, controllerName), HttpStatus.OK);
    }
}
