package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.HeaderRequest;
import com.mes.mesBackend.dto.response.HeaderResponse;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.service.HeaderService;
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

@Tag(name = "header", description = "헤더 API")
@RequestMapping("/headers")
@RestController
@RequiredArgsConstructor
public class HeaderController {

    @Autowired
    HeaderService headerService;

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
            @PathVariable(value = "controller-name") String controllerName
    ) throws NotFoundException {
        return new ResponseEntity<>(headerService.getHeaders(controllerName), HttpStatus.OK);
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
    public ResponseEntity<HeaderResponse> createHeader(@RequestBody @Valid HeaderRequest headerRequest) {
        return new ResponseEntity<>(headerService.createHeader(headerRequest), HttpStatus.OK);
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
            @RequestBody @Valid HeaderRequest headerRequest
    ) {
        return new ResponseEntity<>(headerService.updateHeader(id, headerRequest), HttpStatus.OK);
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
    public ResponseEntity deleteHeader(@PathVariable Long id) {
        headerService.deleteHeader(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
