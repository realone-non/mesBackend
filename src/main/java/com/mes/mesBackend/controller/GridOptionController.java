package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.GridOptionRequest;
import com.mes.mesBackend.dto.response.GridOptionResponse;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.GridOptionService;
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

import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "grid-option", description = "그리드 옵션 API")
@RequestMapping("/users/{user-id}/headers")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class GridOptionController {
    private final GridOptionService gridOptionService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(GridOptionController.class);
    private CustomLogger cLogger;



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
            @RequestBody GridOptionRequest gridOptionRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        GridOptionResponse gridOption = gridOptionService.createGridOption(headerId, gridOptionRequest, userId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + headerId + " from createGridOption.");
        return new ResponseEntity<>(gridOption, OK);
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
            @RequestParam(value = "controller-name") String controllerName,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<HeaderResponse> headerGridOptions = gridOptionService.getHeaderGridOptions(userId, controllerName);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of controllerName: " + controllerName + " from getHeaderGridOptions.");
        return new ResponseEntity<>(headerGridOptions, OK);
    }
}
