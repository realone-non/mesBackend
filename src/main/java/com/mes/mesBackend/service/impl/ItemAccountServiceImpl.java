package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.ItemAccountRequest;
import com.mes.mesBackend.dto.response.ItemAccountResponse;
import com.mes.mesBackend.entity.ItemAccount;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.ItemAccountRepository;
import com.mes.mesBackend.service.ItemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 품목계정
@Service
public class ItemAccountServiceImpl implements ItemAccountService {
    @Autowired
    ItemAccountRepository itemAccountRepository;
    @Autowired
    ModelMapper mapper;

    // 품목계정 생성
    @Override
    public ItemAccountResponse createItemAccount(ItemAccountRequest itemAccountRequest) {
        ItemAccount itemAccount = mapper.toEntity(itemAccountRequest, ItemAccount.class);
        itemAccountRepository.save(itemAccount);
        return mapper.toResponse(itemAccount, ItemAccountResponse.class);
    }

    // 품목계정 단일 조회
    @Override
    public ItemAccountResponse getItemAccount(Long id) throws NotFoundException {
        ItemAccount itemAccount = getItemAccountOrThrow(id);
        return mapper.toResponse(itemAccount, ItemAccountResponse.class);
    }

    // 품목계정 리스트 조회
    @Override
    public List<ItemAccountResponse> getItemAccounts() {
        List<ItemAccount> itemAccounts = itemAccountRepository.findAllByDeleteYnFalse();
        return mapper.toListResponses(itemAccounts, ItemAccountResponse.class);
    }

    // 품목계정 수정
    @Override
    public ItemAccountResponse updateItemAccount(Long id, ItemAccountRequest itemAccountRequest) throws NotFoundException {
        ItemAccount findItemAccount = getItemAccountOrThrow(id);
        ItemAccount newItemAccount = mapper.toEntity(itemAccountRequest, ItemAccount.class);
        findItemAccount.put(newItemAccount);
        itemAccountRepository.save(findItemAccount);
        return mapper.toResponse(findItemAccount, ItemAccountResponse.class);
    }

    // 품목계정 삭제
    @Override
    public void deleteItemAccount(Long id) throws NotFoundException {
        ItemAccount itemAccount = getItemAccountOrThrow(id);
        itemAccount.delete();
        itemAccountRepository.save(itemAccount);
    }

    // 품목계정 조회 및 예외
    @Override
    public ItemAccount getItemAccountOrThrow(Long id) throws NotFoundException {
        ItemAccount itemAccount = itemAccountRepository.findByIdAndDeleteYnFalse(id);
        if (itemAccount == null) throw new NotFoundException("itemAccount does not exist. input id: " + id);
        return itemAccount;
    }
}
