package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.RoutingDetailRequest;
import com.mes.mesBackend.dto.request.RoutingRequest;
import com.mes.mesBackend.dto.response.RoutingDetailResponse;
import com.mes.mesBackend.dto.response.RoutingResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.RoutingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 3-2-5. 라우팅 등록
@Tag(name = "routing", description = "라우팅 API")
@RequestMapping(value = "/routings")
@RestController
@RequiredArgsConstructor
public class RoutingController {

    @Autowired
    RoutingService routingService;

    // 라우팅 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "라우팅 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<RoutingResponse> createRouting(
            @RequestBody @Valid RoutingRequest routingRequest
    ) {
        return new ResponseEntity<>(routingService.createRouting(routingRequest), HttpStatus.OK);
    }

    // 라우팅 단일 조회
    @GetMapping("/{routing-id}")
    @ResponseBody
    @Operation(summary = "라우팅 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<RoutingResponse> getRouting(
            @PathVariable(value = "routing-id") Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(routingService.getRouting(id), HttpStatus.OK);
    }

    // 라우팅 페이징 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "라우팅 페이징 조회")
    @Parameters(
            value = {
                    @Parameter(
                            name = "page", description = "0 부터 시작되는 페이지 (0..N)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "0")
                    ),
                    @Parameter(
                            name = "size", description = "페이지의 사이즈",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "20")
                    ),
                    @Parameter(
                            name = "sort", in = ParameterIn.QUERY,
                            description = "정렬할 대상과 정렬 방식, 데이터 형식: property(,asc|desc). + 디폴트 정렬순서는 오름차순, 다중정렬 가능",
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,desc"))
                    )
            }
    )
    public ResponseEntity<Page<RoutingResponse>> getRoutings(
            @PageableDefault @Parameter(hidden = true) Pageable pageable
    ) {
        return new ResponseEntity<>(routingService.getRoutings(pageable), HttpStatus.OK);
    }

    // 라우팅 수정
    @PatchMapping("/{routing-id}")
    @ResponseBody
    @Operation(summary = "라우팅 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<RoutingResponse> updateRouting(
            @PathVariable(value = "routing-id") Long id,
            @RequestBody @Valid RoutingRequest routingRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(routingService.updateRouting(id, routingRequest), HttpStatus.OK);
    }

    // 라우팅 삭제
    @DeleteMapping("/{routing-id}")
    @ResponseBody
    @Operation(summary = "라우팅 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteRouting(@PathVariable(value = "routing-id") Long id) throws NotFoundException {
        routingService.deleteRouting(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 라우팅 디테일 생성
    @PostMapping("/{routing-id}/routing-details")
    @ResponseBody
    @Operation(summary = "라우팅 디테일 생성")
    public ResponseEntity<RoutingDetailResponse> createRoutingDetail(
            @PathVariable(value = "routing-id") Long routingId,
            @RequestBody @Valid RoutingDetailRequest routingDetailRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(routingService.createRoutingDetails(routingId, routingDetailRequest), HttpStatus.OK);
    }

    // 라우팅 디테일 리스트 조회
    @GetMapping("/{routing-id}/routing-details")
    @ResponseBody
    @Operation(summary = "라우팅 디테일 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<List<RoutingDetailResponse>> getRoutingDetails(
            @PathVariable(value = "routing-id") Long routingId
    ) throws NotFoundException {
        return new ResponseEntity<>(routingService.getRoutingDetails(routingId), HttpStatus.OK);
    }

    // 라우팅 디테일 수정
    @PatchMapping("/{routing-id}/routing-details/{routing-detail-id}")
    @ResponseBody
    @Operation(summary = "라우팅 디테일 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<RoutingDetailResponse> updateRoutingDetail(
            @PathVariable(value = "routing-id") Long routingId,
            @PathVariable(value = "routing-detail-id") Long routingDetailId,
            @RequestBody @Valid RoutingDetailRequest routingDetailRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(routingService.updateRoutingDetail(routingId, routingDetailId, routingDetailRequest), HttpStatus.OK);
    }

    // 라우팅 디테일 삭제
    @DeleteMapping("/{routing-id}/routing-details/{routing-detail-id}")
    @ResponseBody
    @Operation(summary = "라우팅 디테일 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteRoutingDetail(
            @PathVariable(value = "routing-id") Long routingId,
            @PathVariable(value = "routing-detail-id") Long routingDetailId
    ) throws NotFoundException {
        routingService.deleteRoutingDetail(routingId, routingDetailId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
