package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.DevelopStatus;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;


@Getter
@Setter
@Schema(description = "품목")
public class ItemRequest {

    @Schema(description = "품번")
    @NotBlank(message = NOT_EMPTY)
    String itemNo;

    @Schema(description = "품명")
    @NotBlank(message = NOT_EMPTY)
    String itemName;

    @Schema(description = "규격")
    String standard;

    @Schema(description = "품목계정 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long itemAccount;

    @Schema(description = "품목그룹 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long itemGroup;

    @Schema(description = "품목형태 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long itemForm;

    @Schema(description = "용도유형 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long useType;

    @Schema(description = " 라우팅 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long routing;

    @Schema(description = "재고단위  id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long unit;

    @Schema(description = "uhp")
    @NotNull(message = NOT_NULL)
    int uhp;

    @Schema(description = "유효일수")
    @NotNull(message = NOT_NULL)
    int validDay;

    @Schema(description = "LOT유형 id")
    @NotNull(message = NOT_NULL)
    Long lotType;

    @Schema(description = "수입검사")
    @NotNull(message = NOT_NULL)
    TestType inputTest = TestType.NO_TEST;

    @Schema(description = "출하검사")
    @NotNull(message = NOT_NULL)
    TestType outputTest = TestType.NO_TEST;

    @Schema(description = "폐기품 Lot 관리")
    @NotNull(message = NOT_NULL)
    boolean wasteProductLot;

    @Schema(description = "개발상태")
    @NotNull(message = NOT_NULL)
    DevelopStatus developStatus = DevelopStatus.BEFORE;

    @Schema(description = "재고관리")
    @NotNull(message = NOT_NULL)
    boolean stockControl;

    @Schema(description = "입고단가")
    @NotNull(message = NOT_NULL)
    int inputUnitPrice;

    @Schema(description = "저장위치")
    @NotBlank(message = NOT_EMPTY)
    String storageLocation;

    @Schema(description = "거래처품번")
    String clientItemNo;

    @Schema(description = "제조사품번")
    @NotBlank(message = NOT_EMPTY)
    String manufacturerPartNo;

    @Schema(description = "제조사 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long manufacturer;

    @Schema(description = "검사기준 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long testCriteria;

    @Schema(description = "검사방법 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long testProcess;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;

    @Schema(description = "검색어")
    String searchWord;

    @Schema(description = "시효성자재")
    boolean agingMaterialYn;
}
