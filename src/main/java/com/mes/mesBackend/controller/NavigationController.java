package com.mes.mesBackend.controller;

import com.mes.mesBackend.entity.MainNavigation;
import com.mes.mesBackend.entity.SubNavigation;
import com.mes.mesBackend.service.NavigationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/navigations/main-nav")
@Api(tags = "navigation/네비게이션")
@RequiredArgsConstructor
public class NavigationController {

    @Autowired
    NavigationService navigationService;

    // 메인네비게이션바 조회
    @GetMapping("/{name}")
    @ResponseBody
    @ApiOperation(value = "메인네비게이션바 조회")
    public ResponseEntity<List<MainNavigation>> getMainNavigations(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(navigationService.getMainNavigations(name), HttpStatus.OK);
    }

    // 메인네비게이션바 생성
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "메인네비게이션바 생성")
    public ResponseEntity<MainNavigation> createHeader(@RequestBody MainNavigation mainNavigation) {
        return new ResponseEntity<>(navigationService.createMainNavigation(mainNavigation), HttpStatus.OK);
    }

    // 메인네비게이션바 수정
    @PutMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "메인네비게이션바 수정")
    public ResponseEntity<MainNavigation> updateMainNavigation(@PathVariable Long id, @RequestBody MainNavigation mainNavigation) {
        return new ResponseEntity<>(navigationService.updateMainNavigation(id, mainNavigation), HttpStatus.OK);
    }

    // 메인네비게이션바 삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "메인네비게이션바 삭제")
    public ResponseEntity deleteMainNavigation(@PathVariable Long id) {
        navigationService.deleteMainNavigation(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 서브네비게이션바 조회
    @GetMapping("/sub-nav/{name}")
    @ResponseBody
    @ApiOperation(value = "서브네비게이션바 조회")
    public ResponseEntity<List<SubNavigation>> getSubNavigations(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(navigationService.getSubNavigations(name), HttpStatus.OK);
    }

    // 서브네비게이션바 생성
    @PostMapping("/sub-nav")
    @ResponseBody
    @ApiOperation(value = "서브네비게이션바 생성")
    public ResponseEntity<SubNavigation> createSubNavigation(@RequestBody SubNavigation subNavigation) {
        return new ResponseEntity<>(navigationService.createSubNavigation(subNavigation), HttpStatus.OK);
    }

    // 서브네비게이션바 수정
    @PutMapping("/sub-nav/{id}")
    @ResponseBody
    @ApiOperation(value = "서브네비게이션바 수정")
    public ResponseEntity<SubNavigation> updateSubNavigation(@PathVariable Long id, @RequestBody SubNavigation subNavigation) {
        return new ResponseEntity<>(navigationService.updateSubNavigation(id, subNavigation), HttpStatus.OK);
    }

    // 서브네비게이션바 삭제
    @DeleteMapping("/sub-nav/{id}")
    @ResponseBody
    @ApiOperation(value = "서브네비게이션바 삭제")
    public ResponseEntity deleteSubNavigation(@PathVariable Long id) {
        navigationService.deleteSubNavigation(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
