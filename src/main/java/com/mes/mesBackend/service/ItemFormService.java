package com.mes.mesBackend.service;

import com.mes.mesBackend.dto.request.ItemFormRequest;
import com.mes.mesBackend.dto.response.ItemFormResponse;
import com.mes.mesBackend.entity.ItemForm;
import com.mes.mesBackend.exception.NotFoundException;

import java.util.List;

// 품목형태
public interface ItemFormService {
    // 품목형태 생성
    ItemFormResponse createItemForm(ItemFormRequest itemFormRequest);
    // 품목형태 단일 조회
    ItemFormResponse getItemForm(Long id) throws NotFoundException;
    // 품목형태 리스트 조회
    List<ItemFormResponse> getItemForms();
    // 품목형태 수정
    ItemFormResponse updateItemForm(Long id, ItemFormRequest itemFormRequest) throws NotFoundException;
    // 품목형태 삭제
    void deleteItemForm(Long id) throws NotFoundException;
    // 품목형태 조회 및 예외
    ItemForm getItemFormOrThrow(Long id) throws NotFoundException;
}
