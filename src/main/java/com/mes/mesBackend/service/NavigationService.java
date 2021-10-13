package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.DetailNavRequest;
import com.mes.mesBackend.dto.request.MainNavRequest;
import com.mes.mesBackend.dto.request.SubNavRequest;
import com.mes.mesBackend.dto.response.DetailNavResponse;
import com.mes.mesBackend.dto.response.MainNavResponse;
import com.mes.mesBackend.dto.response.SubNavResponse;

import java.util.List;

public interface NavigationService {
    // 메인네비게이션바 조회
    List<MainNavResponse> getMainNavigations();

    // 메인네비게이션바 생성
    MainNavResponse createMainNavigation(MainNavRequest mainNavRequest);

    // 메인네비게이션바 수정
    MainNavResponse updateMainNavigation(Long id, MainNavRequest mainNavRequest);

    // 메인네비게이션바 삭제
    void deleteMainNavigation(Long id);

    // 서브네비게이션바 전체 조회
    List<SubNavResponse> getSubNavigations(Long mainNavId);

    // 서브네이게이션바 생성
    SubNavResponse createSubNavigation(Long mainNavId, SubNavRequest subNavRequest);

    // 서브네이게이션바 수정
    SubNavResponse updateSubNavigation(Long id, SubNavRequest subNavRequest);

    // 서브네이게이션 삭제
    void deleteSubNavigation(Long id);

    // 디테일네비게이션 조회
    List<DetailNavResponse> getDetailNavigations(Long subNavId);

    // 디테일네비게이션 생성
    DetailNavResponse createDetailNavigation(Long subNavId, DetailNavRequest detailNavRequest);

    // 디테일네비게이션 수정
    DetailNavResponse updateDetailNavigation(Long detailNavId, DetailNavRequest detailNavRequest);

    // 디테일네비게이션 삭제
    void deleteDetailNavigation(Long detailNavId);
}
