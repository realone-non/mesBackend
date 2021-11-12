package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.DevelopStatus;
import com.mes.mesBackend.entity.enumeration.TestType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;


@Getter
@Setter
@ApiModel(description = "품목")
public class ItemRequest {

    @ApiModelProperty(value = "품번 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String itemNo;

    @ApiModelProperty(value = "품명 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String itemName;

    @ApiModelProperty(value = "규격")
    String standard;

    @ApiModelProperty(value = "품목계정 id NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long itemAccount;

    @ApiModelProperty(value = "품목그룹 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long itemGroup;

    @ApiModelProperty(value = "품목형태 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long itemForm;

    @ApiModelProperty(value = "용도유형 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long useType;

    @ApiModelProperty(value = " 라우팅 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long routing;

    @ApiModelProperty(value = "재고단위  id NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long unit;

    @ApiModelProperty(value = "uhp NOT NULL")
    @NotNull(message = NOT_NULL)
    int uhp;

    @ApiModelProperty(value = "유효일수 NOT NULL")
    @NotNull(message = NOT_NULL)
    int validDay;

    @ApiModelProperty(value = "LOT유형 id NOT NULL")
    @NotNull(message = NOT_NULL)
    Long lotType;

    @ApiModelProperty(value = "수입검사 NOT NULL")
    @NotNull(message = NOT_NULL)
    TestType inputTest = TestType.NO_TEST;

    @ApiModelProperty(value = "출하검사 NOT NULL")
    @NotNull(message = NOT_NULL)
    TestType outputTest = TestType.NO_TEST;

    @ApiModelProperty(value = "폐기품 Lot 관리 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean wasteProductLot;

    @ApiModelProperty(value = "개발상태 NOT NULL")
    @NotNull(message = NOT_NULL)
    DevelopStatus developStatus = DevelopStatus.BEFORE;

    @ApiModelProperty(value = "재고관리 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean stockControl;

    @ApiModelProperty(value = "입고단가 NOT NULL")
    @NotNull(message = NOT_NULL)
    int inputUnitPrice;

    @ApiModelProperty(value = "저장위치 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String storageLocation;

    @ApiModelProperty(value = "거래처품번")
    String clientItemNo;

    @ApiModelProperty(value = "제조사품번 NOT NULL")
    @NotBlank(message = NOT_EMPTY)
    String manufacturerPartNo;

    @ApiModelProperty(value = "제조사 id NOT NULL")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long manufacturer;

    @ApiModelProperty(value = "검사기준 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long testCriteria;

    @ApiModelProperty(value = "검사방법 id")
    @Min(value = ID_VALUE, message = NOT_ZERO)
    Long testProcess;

    @ApiModelProperty(value = "사용여부 NOT NULL")
    @NotNull(message = NOT_NULL)
    boolean useYn;

    @ApiModelProperty(value = "검색어")
    String searchWord;

    @ApiModelProperty(value = "시효성자재")
    boolean agingMaterialYn;
}
