package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.ItemAccountCode;

import java.util.Optional;

public interface LotMasterRepositoryCustom {
    // id 로 itemAccountCode 의 code 조회
    ItemAccountCode findCodeByItemId(Long itemId);

    // 원부자재 일련번호 시퀀스 찾는법
    // 일련번호의 1~6이 현재날짜의 format 과 동일 And 일련번호가 9자
    // 끝에서 두번째 자리 수 중 제일 큰 애를 찾아서  +1
    Optional<String> findLotNoByLotNoLengthAndLotNoDateAndCode(int length, String date, String code);
}
