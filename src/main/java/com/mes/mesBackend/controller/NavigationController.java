package com.mes.mesBackend.controller;

import com.mes.mesBackend.dto.request.DetailNavRequest;
import com.mes.mesBackend.dto.request.MainNavRequest;
import com.mes.mesBackend.dto.request.SubNavRequest;
import com.mes.mesBackend.dto.response.DetailNavResponse;
import com.mes.mesBackend.dto.response.MainNavResponse;
import com.mes.mesBackend.dto.response.SubNavResponse;
import com.mes.mesBackend.service.NavigationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/navigations/main-navs")
@Api(tags = "navigation")
@RequiredArgsConstructor
public class NavigationController {

    @Autowired
    NavigationService navigationService;

    // 메인네비게이션 조회
    @GetMapping
    @ResponseBody
    @ApiOperation(value = "메인네비게이션 전체 조회")
    public ResponseEntity<List<MainNavResponse>> getMainNavigations() {
        return new ResponseEntity<>(navigationService.getMainNavigations(), HttpStatus.OK);
    }

    // 메인네비게이션 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "메인네비게이션바 생성")
    public ResponseEntity<MainNavResponse> createHeader(@RequestBody @Valid MainNavRequest mainNavRequest) {
        return new ResponseEntity<>(navigationService.createMainNavigation(mainNavRequest), HttpStatus.OK);
    }

    // 메인네비게이션 수정
    @PatchMapping("/{main-nav-id}")
    @ResponseBody
    @ApiOperation(value = "메인네비게이션 수정")
    public ResponseEntity<MainNavResponse> updateMainNavigation(
            @PathVariable(value = "main-nav-id") Long id,
            @RequestBody @Valid MainNavRequest mainNavRequest
    ) {
        return new ResponseEntity<>(navigationService.updateMainNavigation(id, mainNavRequest), HttpStatus.OK);
    }

    // 메인네비게이션바 삭제
    @DeleteMapping("/{main-nav-id}")
    @ResponseBody
    @ApiOperation(value = "메인네비게이션바 삭제")
    public ResponseEntity deleteMainNavigation(@PathVariable(value = "main-nav-id") Long id) {
        navigationService.deleteMainNavigation(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 서브네비게이션바 전체 조회
    @GetMapping("/{main-nav-id}/sub-navs")
    @ResponseBody
    @ApiOperation(value = "서브네비게이션바 조회")
    public ResponseEntity<List<SubNavResponse>> getSubNavigations(@PathVariable(value = "main-nav-id") Long mainNavId) {
        return new ResponseEntity<>(navigationService.getSubNavigations(mainNavId), HttpStatus.OK);
    }

    // 서브네비게이션바 생성
    @PostMapping("/{main-nav-id}/sub-navs")
    @ResponseBody
    @ApiOperation(value = "서브네비게이션바 생성")
    public ResponseEntity<SubNavResponse> createSubNavigation(
            @PathVariable(value = "main-nav-id") Long mainNavId,
            @RequestBody @Valid SubNavRequest subNavRequest
    ) {
        return new ResponseEntity<>(navigationService.createSubNavigation(mainNavId, subNavRequest), HttpStatus.OK);
    }

    // 서브네비게이션바 수정
    @PatchMapping("/sub-navs/{sub-nav-id}")
    @ResponseBody
    @ApiOperation(value = "서브네비게이션바 수정")
    public ResponseEntity<SubNavResponse> updateSubNavigation(
            @PathVariable(value = "sub-nav-id") Long id,
            @RequestBody @Valid SubNavRequest subNavRequest
    ) {
        return new ResponseEntity<>(navigationService.updateSubNavigation(id, subNavRequest), HttpStatus.OK);
    }

    // 서브네비게이션바 삭제
    @DeleteMapping("/sub-navs/{id}")
    @ResponseBody
    @ApiOperation(value = "서브네비게이션바 삭제")
    public ResponseEntity deleteSubNavigation(@PathVariable Long id) {
        navigationService.deleteSubNavigation(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 디테일네비게이션 조회
    @GetMapping("/sub-navs/{sub-nav-id}/detail-navs")
    @ResponseBody
    @ApiOperation(value = "디테일네비게이션 조회")
    public ResponseEntity<List<DetailNavResponse>> getDetailNavigations(
            @PathVariable(value = "sub-nav-id") Long subNavId
    ) {
        return new ResponseEntity<>(navigationService.getDetailNavigations(subNavId), HttpStatus.OK);
    }

    // 디테일네비게이션 생성
    @PostMapping("/sub-navs/{sub-nav-id}/detail-navs")
    @ResponseBody
    @ApiOperation(value = "디테일네비게이션 생성")
    public ResponseEntity<DetailNavResponse> createDetailNavigation(
            @PathVariable(value = "sub-nav-id") Long subNavId,
            @RequestBody @Valid DetailNavRequest detailNavRequest
    ) {
        return new ResponseEntity<>(navigationService.createDetailNavigation(subNavId, detailNavRequest), HttpStatus.OK);
    }

    // 디테일네비게이션 수정
    @PatchMapping("/sub-navs/detail-navs/{detail-nav-id}")
    @ResponseBody
    @ApiOperation(value = "디테일네비게이션 수정")
    public ResponseEntity<DetailNavResponse> updateDetailNavigation(
            @PathVariable(value = "detail-nav-id") Long detailNavId,
            @RequestBody @Valid DetailNavRequest detailNavRequest
    ) {
        return new ResponseEntity<>(navigationService.updateDetailNavigation(detailNavId, detailNavRequest), HttpStatus.OK);
    }

    // 디테일네비게이션 삭제
    @DeleteMapping("/sub-navs/detail-navs/{detail-nav-id}")
    @ResponseBody
    @ApiOperation(value = "디테일네비게이션 삭제")
    public ResponseEntity deleteDetailNavigation(@PathVariable(value = "detail-nav-id") Long detailNavId) {
        navigationService.deleteDetailNavigation(detailNavId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
