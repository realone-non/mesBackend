package com.mes.mesBackend.service.impl;

import com.mes.mesBackend.dto.request.LotMasterRequest;
import com.mes.mesBackend.dto.response.LotMasterResponse;
import com.mes.mesBackend.entity.ItemAccountCode;
import com.mes.mesBackend.entity.LotMaster;
import com.mes.mesBackend.entity.LotType;
import com.mes.mesBackend.entity.PurchaseInput;
import com.mes.mesBackend.entity.enumeration.EnrollmentType;
import com.mes.mesBackend.exception.BadRequestException;
import com.mes.mesBackend.exception.NotFoundException;
import com.mes.mesBackend.mapper.ModelMapper;
import com.mes.mesBackend.repository.LotMasterRepository;
import com.mes.mesBackend.repository.PurchaseInputRepository;
import com.mes.mesBackend.service.ItemService;
import com.mes.mesBackend.service.LotMasterService;
import com.mes.mesBackend.service.LotTypeService;
import com.mes.mesBackend.service.WareHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mes.mesBackend.helper.Constants.LOT_DEFAULT_SEQ;
import static com.mes.mesBackend.helper.Constants.YYMMDD;

@Service
@RequiredArgsConstructor
public class LotMasterServiceImpl implements LotMasterService {
    private final LotMasterRepository lotMasterRepo;
    private final PurchaseInputRepository purchaseInputRepo;
    private final ModelMapper modelMapper;
    private final LotTypeService lotTypeService;
    private final ItemService itemService;
    private final WareHouseService wareHouseService;

    // LOT master 생성
    @Override
    public String createLotMaster(LotMasterRequest lotMasterRequest) throws NotFoundException, BadRequestException {
        LotType lotType = lotMasterRequest.getLotTypeId() != null ? lotTypeService.getLotTypeOrThrow(lotMasterRequest.getLotTypeId()) : null;
        PurchaseInput purchaseInput = lotMasterRequest.getPurchaseInputId() != null ? getPurchaseInputOrThrow(lotMasterRequest.getPurchaseInputId()) : null;

        LotMaster lotMaster = modelMapper.toEntity(lotMasterRequest, LotMaster.class);

        // 구매입고
        if (purchaseInput != null) {
            Long itemId = purchaseInputRepo.findItemIdByPurchaseInputId(purchaseInput.getId());
            String lotNo = createLotNo(itemId, purchaseInput.getId());
            lotMaster.putPurchaseInput(lotType, purchaseInput, lotNo); // 등록유형 PURCHASE_INPUT
        }

        lotMasterRepo.save(lotMaster);
        return lotMaster.getLotNo();
    }

    // lot 번호 생성
    private String createLotNo(Long itemId, Long deleteId) throws BadRequestException {
        // 1~6 입고년월일 예) 21년 12월 11일 211211
        String dateCode = LocalDate.now().format(DateTimeFormatter.ofPattern(YYMMDD));

        // 품목의 품목계정 코드 조회
        ItemAccountCode itemAccountCode = lotMasterRepo.findCodeByItemId(itemId);
        String code = itemAccountCode.getCode();
        String beforeLotNo;

        // 조회할 length 를 itemAccountId 별로 반환
        int length = (itemAccountCode.getItemAccount().getId() == 1) ? 9
                : (itemAccountCode.getItemAccount().getId() == 2) ? 10
                : (itemAccountCode.getItemAccount().getId() == 3) ? 11
                : (itemAccountCode.getItemAccount().getId() == 4) ? 13
                : 0;

        if (length == 0) throw new BadRequestException("등록된 품목계정이 일치하지 않습니다.");

        // lot 번호 생성
        // 품목계정: 원자재(211229H01), 부자재(211229PT01)
        if (itemAccountCode.getItemAccount().getAccount().equals("원자재") || itemAccountCode.getItemAccount().getAccount().equals("부자재")) {
            // lotNo 길이가 9글자 && 현재날짜 포함 && code 포함 && desc
            // else -> 00으로
            beforeLotNo = lotMasterRepo.findLotNoByLotNoLengthAndLotNoDateAndCode(length, dateCode, code)
                    .orElse(dateCode + code + LOT_DEFAULT_SEQ);
            int beforeSubString =  Integer.parseInt(beforeLotNo.substring(beforeLotNo.length()-2));
            int seq = beforeSubString + 1;
            return dateCode + code + String.format("%02d", seq);
        } else {
            purchaseInputRepo.deleteById(deleteId);
            throw new BadRequestException("품목계정의 이름이 '원자재', '부자재' 와 일치하지 않습니다. 등록되어 있는 품목계정 명: " + itemAccountCode.getItemAccount().getAccount());
        }
    }

    // 구매입고 단일 조회 및 예외
    public PurchaseInput getPurchaseInputOrThrow(Long id) throws NotFoundException {
        return purchaseInputRepo.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new NotFoundException("purchaseInput does not exist. input purchaseInput id: " + id));
    }

    // 7-1
    // LOT 마스터 조회, 검색조건: 품목그룹 id, LOT 번호, 품번|품명, 창고 id, 등록유형, 재고유무, LOT 유형, 검사중여부
    @Override
    public List<LotMasterResponse> getLotMasters(
            Long itemGroupId,
            String lotNo,
            String itemNoAndItemName,
            Long wareHouseId,
            EnrollmentType enrollmentType,
            Boolean stockYn,
            Long lotTypeId,
            Boolean testingYn
    ) {
        return lotMasterRepo.findLotMastersByCondition(itemGroupId, lotNo, itemNoAndItemName, wareHouseId, enrollmentType, stockYn, lotTypeId, testingYn);
    }
}
