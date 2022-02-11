package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.DetailNavRequest;
import com.mes.mesBackend.dto.request.MainNavRequest;
import com.mes.mesBackend.dto.request.SubNavRequest;
import com.mes.mesBackend.dto.response.DetailNavResponse;
import com.mes.mesBackend.dto.response.MainNavResponse;
import com.mes.mesBackend.dto.response.SubNavResponse;
import com.mes.mesBackend.logger.CustomLogger;
import com.mes.mesBackend.logger.LogService;
import com.mes.mesBackend.logger.MongoLogger;
import com.mes.mesBackend.service.NavigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.MONGO_TEMPLATE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "navigation", description = "네비게이션 API")
@RequestMapping("/navigations/main-navs")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
public class NavigationController {
    private final NavigationService navigationService;
    private final LogService logService;
    private final Logger logger = LoggerFactory.getLogger(NavigationController.class);
    private CustomLogger cLogger;

    // 메인네비게이션 조회
    @GetMapping
    @ResponseBody
    @Operation(summary = "메인네비게이션 전체 조회")
    public ResponseEntity<List<MainNavResponse>> getMainNavigations(
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<MainNavResponse> mainNavigations = navigationService.getMainNavigations();
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of from getMainNavigations.");
        return new ResponseEntity<>(mainNavigations, OK);
    }

    // 메인네비게이션 생성
    @PostMapping
    @ResponseBody
    @Operation(summary = "메인네비게이션바 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<MainNavResponse> createMainNavigation(
            @RequestBody @Valid MainNavRequest mainNavRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        MainNavResponse mainNavigation = navigationService.createMainNavigation(mainNavRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + mainNavigation.getId() + " from createMainNavigation.");
        return new ResponseEntity<>(mainNavigation, OK);
    }

    // 메인네비게이션 수정
    @PatchMapping("/{main-nav-id}")
    @ResponseBody
    @Operation(summary = "메인네비게이션 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<MainNavResponse> updateMainNavigation(
            @PathVariable(value = "main-nav-id") Long id,
            @RequestBody @Valid MainNavRequest mainNavRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        MainNavResponse mainNav = navigationService.updateMainNavigation(id, mainNavRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + mainNav.getId() + " from updateMainNavigation.");
        return new ResponseEntity<>(mainNav, OK);
    }

    // 메인네비게이션바 삭제
    @DeleteMapping("/{main-nav-id}")
    @ResponseBody
    @Operation(summary = "메인네비게이션바 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteMainNavigation(
            @PathVariable(value = "main-nav-id") Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        navigationService.deleteMainNavigation(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteMainNavigation.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 서브네비게이션바 전체 조회
    @GetMapping("/{main-nav-id}/sub-navs")
    @ResponseBody
    @Operation(summary = "서브네비게이션바 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<SubNavResponse>> getSubNavigations(
            @PathVariable(value = "main-nav-id") Long mainNavId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<SubNavResponse> subNavigations = navigationService.getSubNavigations(mainNavId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of mainNavId: " + mainNavId +
                " from getSubNavigations.");
        return new ResponseEntity<>(subNavigations, OK);
    }

    // 서브네비게이션바 생성
    @PostMapping("/{main-nav-id}/sub-navs")
    @ResponseBody
    @Operation(summary = "서브네비게이션바 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<SubNavResponse> createSubNavigation(
            @PathVariable(value = "main-nav-id") Long mainNavId,
            @RequestBody @Valid SubNavRequest subNavRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        SubNavResponse subNavigation = navigationService.createSubNavigation(mainNavId, subNavRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + subNavigation.getId() + " from createSubNavigation.");
        return new ResponseEntity<>(subNavigation, OK);
    }

    // 서브네비게이션바 수정
    @PatchMapping("/sub-navs/{sub-nav-id}")
    @ResponseBody
    @Operation(summary = "서브네비게이션바 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<SubNavResponse> updateSubNavigation(
            @PathVariable(value = "sub-nav-id") Long id,
            @RequestBody @Valid SubNavRequest subNavRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        SubNavResponse subNav = navigationService.updateSubNavigation(id, subNavRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + subNav.getId() + " from updateSubNavigation.");
        return new ResponseEntity<>(subNav, OK);
    }

    // 서브네비게이션바 삭제
    @DeleteMapping("/sub-navs/{id}")
    @ResponseBody
    @Operation(summary = "서브네비게이션바 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteSubNavigation(
            @PathVariable Long id,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        navigationService.deleteSubNavigation(id);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + id + " from deleteSubNavigation.");
        return new ResponseEntity(NO_CONTENT);
    }

    // 디테일네비게이션 조회
    @GetMapping("/sub-navs/{sub-nav-id}/detail-navs")
    @ResponseBody
    @Operation(summary = "디테일네비게이션 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
            }
    )
    public ResponseEntity<List<DetailNavResponse>> getDetailNavigations(
            @PathVariable(value = "sub-nav-id") Long subNavId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        List<DetailNavResponse> detailNavigations = navigationService.getDetailNavigations(subNavId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is viewed the list of subNavId: " + subNavId +
                " from getDetailNavigations.");
        return new ResponseEntity<>(detailNavigations, OK);
    }

    // 디테일네비게이션 생성
    @PostMapping("/sub-navs/{sub-nav-id}/detail-navs")
    @ResponseBody
    @Operation(summary = "디테일네비게이션 생성")
    public ResponseEntity<DetailNavResponse> createDetailNavigation(
            @PathVariable(value = "sub-nav-id") Long subNavId,
            @RequestBody @Valid DetailNavRequest detailNavRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        DetailNavResponse detailNavigation = navigationService.createDetailNavigation(subNavId, detailNavRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is created the " + detailNavigation.getId() + " from createDetailNavigation.");
        return new ResponseEntity<>(detailNavigation, OK);
    }

    // 디테일네비게이션 수정
    @PatchMapping("/sub-navs/detail-navs/{detail-nav-id}")
    @ResponseBody
    @Operation(summary = "디테일네비게이션 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found resource"),
                    @ApiResponse(responseCode = "400", description = "bad request")
            }
    )
    public ResponseEntity<DetailNavResponse> updateDetailNavigation(
            @PathVariable(value = "detail-nav-id") Long detailNavId,
            @RequestBody @Valid DetailNavRequest detailNavRequest,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        DetailNavResponse detailNavigation = navigationService.updateDetailNavigation(detailNavId, detailNavRequest);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is modified the " + detailNavigation.getId() + " from updateDetailNavigation.");
        return new ResponseEntity<>(detailNavigation, OK);
    }

    // 디테일네비게이션 삭제
    @DeleteMapping("/sub-navs/detail-navs/{detail-nav-id}")
    @ResponseBody
    @Operation(summary = "디테일네비게이션 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "no content"),
                    @ApiResponse(responseCode = "404", description = "not found resource")
            }
    )
    public ResponseEntity deleteDetailNavigation(
            @PathVariable(value = "detail-nav-id") Long detailNavId,
            @RequestHeader(value = AUTHORIZATION, required = false) @Parameter(hidden = true) String tokenHeader
    ) {
        navigationService.deleteDetailNavigation(detailNavId);
        cLogger = new MongoLogger(logger, MONGO_TEMPLATE);
        cLogger.info(logService.getUserCodeFromHeader(tokenHeader) + " is deleted the " + detailNavId + " from deleteDetailNavigation.");
        return new ResponseEntity(NO_CONTENT);
    }
}
