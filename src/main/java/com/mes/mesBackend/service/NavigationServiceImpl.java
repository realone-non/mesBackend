package com.mes.mesBackend.service;

import com.mes.mesBackend.entity.MainNavigation;
import com.mes.mesBackend.entity.SubNavigation;
import com.mes.mesBackend.repository.MainNavigationRepository;
import com.mes.mesBackend.repository.SubNavigationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavigationServiceImpl implements NavigationService {

    @Autowired MainNavigationRepository mainNavigationRepository;
    @Autowired SubNavigationRepository subNavigationRepository;

    // 메인네비게이션바 조회
    public List<MainNavigation> getMainNavigations(String mainNavName) {
        return mainNavigationRepository.findMainNavigationsByUseYnTrue();
    }

    // 메인네비게이션바 생성
    public MainNavigation createMainNavigation(MainNavigation mainNavigation) {
        return mainNavigationRepository.save(mainNavigation);
    }

    // 메인네비게이션바 수정
    public MainNavigation updateMainNavigation(Long id, MainNavigation newMainNavigation) {
        MainNavigation findMainNavigation = mainNavigationRepository.findByIdAndUseYnTrue(id);
        // update Navigation
        findMainNavigation.put(newMainNavigation);
        return mainNavigationRepository.save(findMainNavigation);
    }

    // 메인네비게이션바 삭제
    public void deleteMainNavigation(Long id) {
        MainNavigation findMainNavigation = mainNavigationRepository.findByIdAndUseYnTrue(id);
        findMainNavigation.setUseYn(false);
    }

    // 서브네비게이션바 전체 조회
    public List<SubNavigation> getSubNavigations(String subNavName) {
        return subNavigationRepository.findSubNavigationsByUseYnTrue();
    }

    // 서브네이게이션바 생성
    public SubNavigation createSubNavigation(SubNavigation subNavigation) {
        return subNavigationRepository.save(subNavigation);
    }

    // 서브네이게이션바 수정
    public SubNavigation updateSubNavigation(Long id, SubNavigation subNavigation) {
        SubNavigation findSubNavigation = subNavigationRepository.findByIdAndUseYnTrue(id);
        findSubNavigation.put(subNavigation);
        return subNavigationRepository.save(findSubNavigation);
    }

    // 서브네이게이션 삭제
    public void deleteSubNavigation(Long id) {
        SubNavigation findSubNavigation = subNavigationRepository.findByIdAndUseYnTrue(id);
        findSubNavigation.setUseYn(false);
    }
}
