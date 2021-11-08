package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.RoutingDetailRequest;
import com.mes.mesBackend.dto.request.RoutingRequest;
import com.mes.mesBackend.dto.response.RoutingDetailResponse;
import com.mes.mesBackend.dto.response.RoutingResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.RoutingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 3-2-5. 라우팅 등록
@RestController
@RequestMapping(value = "/routings")
@Api(tags = "routing")
@RequiredArgsConstructor
public class RoutingController {

    @Autowired
    RoutingService routingService;

    // 라우팅 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "라우팅 생성")
    public ResponseEntity<RoutingResponse> createRouting(@RequestBody RoutingRequest routingRequest) {
        return new ResponseEntity<RoutingResponse>(routingService.createRouting(routingRequest), HttpStatus.OK);
    }

    // 라우팅 단일 조회
    @GetMapping("/{routing-id}")
    @ResponseBody
    @ApiOperation(value = "라우팅 단일 조회")
    public ResponseEntity<RoutingResponse> getRouting(@PathVariable(value = "routing-id") Long id) throws NotFoundException {
        return new ResponseEntity<RoutingResponse>(routingService.getRouting(id), HttpStatus.OK);
    }

    // 라우팅 페이징 조회
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "라우팅 페이징 조회")
    public ResponseEntity<Page<RoutingResponse>> getRoutings(@PageableDefault Pageable pageable) {
        return new ResponseEntity<Page<RoutingResponse>>(routingService.getRoutings(pageable), HttpStatus.OK);
    }

    // 라우팅 수정
    @PatchMapping("/{routing-id}")
    @ResponseBody
    @ApiOperation(value = "라우팅 수정")
    public ResponseEntity<RoutingResponse> updateRouting(
            @PathVariable(value = "routing-id") Long id,
            @RequestBody RoutingRequest routingRequest
    ) throws NotFoundException {
        return new ResponseEntity<RoutingResponse>(routingService.updateRouting(id, routingRequest), HttpStatus.OK);
    }

    // 라우팅 삭제
    @DeleteMapping("/{routing-id}")
    @ResponseBody
    @ApiOperation(value = "라우팅 삭제")
    public ResponseEntity deleteRouting(@PathVariable(value = "routing-id") Long id) throws NotFoundException {
        routingService.deleteRouting(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 라우팅 디테일 생성
    @PostMapping("/{routing-id}/routing-details")
    @ResponseBody
    @ApiOperation(value = "라우팅 디테일 생성")
    public ResponseEntity<RoutingDetailResponse> createRoutingDetail(
            @PathVariable(value = "routing-id") Long routingId,
            @RequestBody RoutingDetailRequest routingDetailRequest
    ) throws NotFoundException {
        return new ResponseEntity<RoutingDetailResponse>(routingService.createRoutingDetails(routingId, routingDetailRequest), HttpStatus.OK);
    }

    // 라우팅 디테일 리스트 조회
    @GetMapping("/{routing-id}/routing-details")
    @ResponseBody
    @ApiOperation(value = "라우팅 디테일 리스트 조회")
    public ResponseEntity<List<RoutingDetailResponse>> getRoutingDetails(
            @PathVariable(value = "routing-id") Long routingId
    ) throws NotFoundException {
        return new ResponseEntity<List<RoutingDetailResponse>>(routingService.getRoutingDetails(routingId), HttpStatus.OK);
    }

    // 라우팅 디테일 수정
    @PatchMapping("/{routing-id}/routing-details/{routing-detail-id}")
    @ResponseBody
    @ApiOperation(value = "라우팅 디테일 수정")
    public ResponseEntity<RoutingDetailResponse> updateRoutingDetail(
            @PathVariable(value = "routing-id") Long routingId,
            @PathVariable(value = "routing-detail-id") Long routingDetailId,
            @RequestBody RoutingDetailRequest routingDetailRequest
    ) throws NotFoundException {
        return new ResponseEntity<RoutingDetailResponse>(routingService.updateRoutingDetail(routingId, routingDetailId, routingDetailRequest), HttpStatus.OK);
    }

    // 라우팅 디테일 삭제
    @DeleteMapping("/{routing-id}/routing-details/{routing-detail-id}")
    @ResponseBody
    @ApiOperation(value = "라우팅 디테일 삭제")
    public ResponseEntity deleteRoutingDetail(
            @PathVariable(value = "routing-id") Long routingId,
            @PathVariable(value = "routing-detail-id") Long routingDetailId
    ) throws NotFoundException {
        routingService.deleteRoutingDetail(routingId, routingDetailId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
