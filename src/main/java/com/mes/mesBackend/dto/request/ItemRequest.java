package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.DevelopStatus;
import com.mes.mesBackend.entity.enumeration.TestType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;


@Getter
@Setter
public class ItemRequest {
    @NotBlank(message = NOT_EMPTY)
    String itemNo;// 품번

    @NotBlank(message = NOT_EMPTY)
    String itemName;                                // 품명

    String standard;                                // 규격

    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long itemAccount;                             // 품목계정

    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long itemGroup;                               // 품목그룹

    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long itemForm;                                 // 품목형태

    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long useType;                                   // 용도유형

    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long routing;                                    // 라우팅 (라우팅 명)

    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long unit;                                          // 재고단위

    @NotNull(message = NOT_NULL)
    int uhp;                                       // uhp

    @NotNull(message = NOT_NULL)
    int validDay;                                  // 유효일수

    @NotNull(message = NOT_NULL)
    Long lotType;                               // LOT유형

    @NotNull(message = NOT_NULL)
    TestType inputTest = TestType.NO_TEST;              // 수입검사

    @NotNull(message = NOT_NULL)
    TestType outputTest = TestType.NO_TEST;             // 출하검사

    @NotNull(message = NOT_NULL)
    boolean wasteProductLot;                                   // 폐기품 Lot 관리

    @NotNull(message = NOT_NULL)
    DevelopStatus developStatus = DevelopStatus.BEFORE;           // 개발상태

    @NotNull(message = NOT_NULL)
    boolean stockControl;                                  // 재고관리

    @NotNull(message = NOT_NULL)
    int inputUnitPrice;                                    // 입고단가

    @NotBlank(message = NOT_EMPTY)
    String storageLocation;                               // 저장위치

    String clientItemNo;                                   // 거래처 품번

    @NotBlank(message = NOT_EMPTY)
    String manufacturerPartNo;                                   // 제조사품번

    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long manufacturer;                                 // 제조사

    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long testCriteria;                                 // 검사기준

    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long testProcess;                                   // 검사방법

    @NotNull(message = NOT_NULL)
    boolean useYn;                                                 // 사용

    String searchWord;                                     // 검색어

    boolean agingMaterialYn;                                 // 시효성자재
}
