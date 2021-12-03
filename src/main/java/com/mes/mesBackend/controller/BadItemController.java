package com.mes.mesBackend.controller;

import com.mes.mesBackend.auth.JwtTokenProvider;
import com.mes.mesBackend.dto.request.BadItemRequest;
import com.mes.mesBackend.dto.response.BadItemResponse;
import com.mes.mesBackend.entity.BadItem;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.BadItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequestMapping(value = "/bad-items")
@Tag(name = "bad-item", description = "불량항목 API")
@RestController
@SecurityRequirement(name = "Authorization")
@Slf4j
public class BadItemController {
    @Autowired
    BadItemService badItemService;
    @Autowired
    LogService logService;

    private Logger logger = LoggerFactory.getLogger(BadItemController.class);
    private CustomLogger cLogger;

    @Operation(summary = "불량항목 생성", description = "")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<BadItemResponse> createBadItem(
            @RequestBody @Valid BadItemRequest badItemRequest,
            @RequestHeader(value = "Authorization") String header
    ) throws BadRequestException {
        cLogger = new MongoLogger(logger, "mongoTemplate");

        BadItemResponse badItem = badItemService.createBadItem(badItemRequest);
        cLogger.trace("test", logService.getUserCode(header), badItem.getId());

        return new ResponseEntity<>(badItem, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "불량항목 단일 조회", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<BadItemResponse> getBadItem(
            @PathVariable(value = "id") Long id
    ) throws NotFoundException {
        return new ResponseEntity<>(badItemService.getBadItem(id), HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "불량항목 전체 조회", description = "")
    public ResponseEntity<List<BadItemResponse>> getBadItems() {
        return new ResponseEntity<>(badItemService.getBadItems(), HttpStatus.OK);
    }


    @PatchMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "불량항목 수정", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<BadItemResponse> updateBadItem(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid BadItemRequest badItemRequest
    ) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(badItemService.updateBadItem(id, badItemRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody()
    @Operation(summary = "불량항목 삭제", description = "")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity<Void> deleteBadItem(@PathVariable(value = "id") Long id) throws NotFoundException {
        badItemService.deleteBadItem(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    //    @GetMapping
//    @ResponseBody
//    @Operation(summary = "불량항목  조회", description = "")
//    @Parameters(
//            value = {
//                    @Parameter(
//                            name = "page", description = "0 부터 시작되는 페이지 (0..N)",
//                            in = ParameterIn.QUERY,
//                            schema = @Schema(type = "integer", defaultValue = "0")
//                    ),
//                    @Parameter(
//                            name = "size", description = "페이지의 사이즈",
//                            in = ParameterIn.QUERY,
//                            schema = @Schema(type = "integer", defaultValue = "20")
//                    ),
//                    @Parameter(
//                            name = "sort", in = ParameterIn.QUERY,
//                            description = "정렬할 대상과 정렬 방식, 데이터 형식: property(,asc|desc). + 디폴트 정렬순서는 오름차순, 다중정렬 가능",
//                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,desc"))
//                    )
//            }
//    )
//    public ResponseEntity<Page<BadItemResponse>> getBadItems(
//            @PageableDefault @Parameter(hidden = true) Pageable pageable
//    ) {
//        return new ResponseEntity<>(badItemService.getBadItems(pageable), HttpStatus.OK);
//    }
}
