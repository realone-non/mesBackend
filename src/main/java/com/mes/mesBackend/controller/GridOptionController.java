package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.GridOptionRequest;
import com.mes.mesBackend.dto.response.GridOptionResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.GridOptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grid-options")
@Api(tags = "[구현중] grid-option")
@RequiredArgsConstructor
public class GridOptionController {

    @Autowired
    GridOptionService gridOptionService;

//    @GetMapping("/{controller-name}/{user-id}")
//    @ResponseBody
//    @ApiOperation(value = "그리드 옵션 다중조회")
//    public ResponseEntity<List<GridOptionResponse>> getGridOptions(
//            @PathVariable(value = "controller-name") String controllerName,
//            @PathVariable(value = "user-id") Long userId)
//    {
//        return new ResponseEntity<>(gridOptionService.getGrids(controllerName, userId), HttpStatus.OK);
//    }

//    @PostMapping("/{user-id}/{controller-name}")
//    @ResponseBody
//    @ApiOperation(value = "그리드 옵션 다중생성")
//    public ResponseEntity<List<GridOptionResponse>> createGridOptions(
//            @PathVariable(value = "user-id") Long userId,
////            @PathVariable(value = "controller-name") String controllerName,
//            @RequestBody List<GridOptionRequest> gridOptionRequest
//            ) {
//        return new ResponseEntity<>(gridOptionService.createGridOptions(userId, gridOptionRequest), HttpStatus.OK);
//    }

    @PostMapping("/{header-id}/{controller-name}")
    @ResponseBody
    @ApiOperation(value = "그리드 옵션 단일 생성")
    public ResponseEntity<GridOptionResponse> createGridOption(
            @PathVariable(value = "header-id") Long headerId,
            @PathVariable(value = "controller-name") String controllerName,
            @RequestBody GridOptionRequest gridOptionRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(gridOptionService.createGridOption(headerId, controllerName, gridOptionRequest), HttpStatus.OK);
    }
}
