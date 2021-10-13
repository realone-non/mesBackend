package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.MainNavigation;
import com.mes.mesBackend.entity.SubNavigation;

import java.util.List;

public interface NavigationService {
    // 메인네비게이션바 조회
    List<MainNavigation> getMainNavigations(String mainNavName);

    // 메인네비게이션바 생성
    MainNavigation createMainNavigation(MainNavigation mainNavigation);

    // 메인네비게이션바 수정
    MainNavigation updateMainNavigation(Long id, MainNavigation mainNavigation);

    // 메인네비게이션바 삭제
    void deleteMainNavigation(Long id);

    // 서브네비게이션바 전체 조회
    List<SubNavigation> getSubNavigations(String subNavName);

    // 서브네이게이션바 생성
    SubNavigation createSubNavigation(SubNavigation subNavigation);

    // 서브네이게이션바 수정
    SubNavigation updateSubNavigation(Long id, SubNavigation subNavigation);

    // 서브네이게이션 삭제
    void deleteSubNavigation(Long id);
}
