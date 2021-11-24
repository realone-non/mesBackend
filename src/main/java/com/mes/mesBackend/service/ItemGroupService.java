package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.CodeRequest;
import com.mes.mesBackend.dto.request.ItemGroupRequest;
import com.mes.mesBackend.dto.response.CodeResponse;
import com.mes.mesBackend.dto.response.ItemGroupResponse;
import com.mes.mesBackend.entity.ItemGroup;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 품목그룹
public interface ItemGroupService {
    // 품목그룹 생성
    ItemGroupResponse createItemGroup(ItemGroupRequest itemGroupRequest) throws NotFoundException;
    // 품목그룹 단일 조회
    ItemGroupResponse getItemGroup(Long id) throws NotFoundException;
    // 품목그룹 리스트 조회
    List<ItemGroupResponse> getItemGroups();
    // 품목그룹 페이징 조회
//    Page<ItemGroupResponse> getItemGroups(Pageable pageable);
    // 품목그룹 수정
    ItemGroupResponse updateItemGroup(Long id, ItemGroupRequest itemGroupRequest) throws NotFoundException;
    // 품목그룹 삭제
    void deleteItemGroup(Long id) throws NotFoundException;
    // 품목그룹 조회 및 예외
    ItemGroup getItemGroupOrThrow(Long id) throws NotFoundException;

    // 그룹코드 생성
    CodeResponse createItemGroupCode(CodeRequest codeRequest);
    // 그룹코드 단일 조회
    CodeResponse getItemGroupCode(Long id) throws NotFoundException;
    // 그룹코드 리스트 조회
    List<CodeResponse> getItemGroupCodes();
    // 그룹코드 삭제
    void deleteItemGroupCode(Long id) throws NotFoundException, BadRequestException;
}
