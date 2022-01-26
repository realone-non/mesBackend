package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.dto.response.ItemAccountCodeResponse;

import java.util.List;

public interface ItemAccountCodeRepositoryCustom {
    List<ItemAccountCodeResponse> findItemAccountCodeResponseByItemAccountId(Long itemAccountId);
}
