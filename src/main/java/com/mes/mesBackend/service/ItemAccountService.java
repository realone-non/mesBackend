package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ItemAccountRequest;
import com.mes.mesBackend.dto.response.ItemAccountResponse;
import com.mes.mesBackend.entity.ItemAccount;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;
// 품목계정
public interface ItemAccountService {
    // 품목계정 생성
    ItemAccountResponse createItemAccount(ItemAccountRequest itemAccountRequest);
    // 품목계정 단일 조회
    ItemAccountResponse getItemAccount(Long id) throws NotFoundException;
    // 품목계정 리스트 조회
    List<ItemAccountResponse> getItemAccounts();
    // 품목계정 수정
    ItemAccountResponse updateItemAccount(Long id, ItemAccountRequest itemAccountRequest) throws NotFoundException;
    // 품목계정 삭제
    void deleteItemAccount(Long id) throws NotFoundException;
    // 품목계정 조회 및 예외
    ItemAccount getItemAccountOrThrow(Long id) throws NotFoundException;
}
