package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.HeaderRequest;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.HeaderService;
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

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "header", description = "헤더 API")
@RequestMapping("/headers")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class HeaderController {
    private final HeaderService headerService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(HeaderController.class);
    private CustomLogger cLogger;

    // 헤더조회
    @GetMapping("/{controller-name}")
    @ResponseBody
    @Operation(summary = "헤더 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<HeaderResponse>> getHeaders(
            @PathVariable(value = "controller-name") String controllerName,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) throws NotFoundException {
        List<HeaderResponse> headers = headerService.getHeaders(controllerName);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the controllerName: " + controllerName + " from getHeaders.");
        return new ResponseEntity<>(headers, OK);
    }

    // 헤더생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "헤더 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<HeaderResponse> createHeader(
            @RequestBody @Valid HeaderRequest headerRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        HeaderResponse header = headerService.createHeader(headerRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the header: " + headerRequest.getHeader()  + " from createHeader.");
        return new ResponseEntity<>(header, OK);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    @Operation(summary = "헤더 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<HeaderResponse> updateHeader(
            @PathVariable Long id,
            @RequestBody @Valid HeaderRequest headerRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        HeaderResponse header = headerService.updateHeader(id, headerRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + header.getId() + " from updateHeader.");
        return new ResponseEntity<>(header, OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "헤더 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity deleteHeader(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        headerService.deleteHeader(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteHeader.");
        return new ResponseEntity(NO_CONTENT);
    }
}
