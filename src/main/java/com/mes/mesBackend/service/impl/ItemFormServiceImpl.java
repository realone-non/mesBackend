package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ItemFormRequest;
import com.mes.mesBackend.dto.response.ItemFormResponse;
import com.mes.mesBackend.entity.ItemForm;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ItemFormRepository;
import com.mes.mesBackend.service.ItemFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 품목형태
@Service
public class ItemFormServiceImpl implements ItemFormService {
    @Autowired
    ItemFormRepository itemFormRepository;
    @Autowired
    ModelMapper mapper;

    // 품목형태 생성
    @Override
    public ItemFormResponse createItemForm(ItemFormRequest itemFormRequest) {
        ItemForm itemForm = mapper.toEntity(itemFormRequest, ItemForm.class);
        itemFormRepository.save(itemForm);
        return mapper.toResponse(itemForm, ItemFormResponse.class);
    }

    // 품목형태 단일 조회
    @Override
    public ItemFormResponse getItemForm(Long id) throws NotFoundException {
        ItemForm itemForm = getItemFormOrThrow(id);
        return mapper.toResponse(itemForm, ItemFormResponse.class);
    }

    // 품목형태 리스트 조회
    @Override
    public List<ItemFormResponse> getItemForms() {
        List<ItemForm> itemForms = itemFormRepository.findAllByDeleteYnFalse();
        return mapper.toListResponses(itemForms, ItemFormResponse.class);
    }

    // 품목형태 수정
    @Override
    public ItemFormResponse updateItemForm(Long id, ItemFormRequest itemFormRequest) throws NotFoundException {
        ItemForm findItemForm = getItemFormOrThrow(id);
        ItemForm newItemForm = mapper.toEntity(itemFormRequest, ItemForm.class);
        findItemForm.put(newItemForm);
        itemFormRepository.save(findItemForm);
        return mapper.toResponse(findItemForm, ItemFormResponse.class);
    }

    // 품목형태 삭제
    @Override
    public void deleteItemForm(Long id) throws NotFoundException {
        ItemForm itemForm = getItemFormOrThrow(id);
        itemForm.delete();
        itemFormRepository.save(itemForm);
    }

    // 품목형태 조회 및 예외
    @Override
    public ItemForm getItemFormOrThrow(Long id) throws NotFoundException {
        ItemForm itemForm = itemFormRepository.findByIdAndDeleteYnFalse(id);
        if (itemForm == null) throw new NotFoundException("itemForm does not exist. input id: " + id);
        return itemForm;
    }
}
