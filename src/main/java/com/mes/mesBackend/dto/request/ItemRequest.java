package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.DevelopStatus;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.entity.enumeration.TestCategory;
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

    @Schema(description = "품목계정코드 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long itemAccountCode;

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

    @Schema(description = "LOT 유형 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long lotType;

    @Schema(description = "폐기품 Lot 관리")
    @NotNull(message = NOT_NULL)
    boolean wasteProductLot;

    @Schema(description = "개발상태 [BEFORE : 미개발, PROCEEDING : 개발중, COMPLETION : 개발완료]")
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

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;

    @Schema(description = "검색어")
    String searchWord;

    @Schema(description = "시효성자재")
    boolean agingMaterialYn;

    @Schema(description = "검사종류 [수입검사: INPUT_TEST, 공정검사: PROCESS_TEST, 출하검사: OUTPUT_TEST]")
    @NotNull(message = NOT_NULL)
    TestCategory testCategory;      // 검사종류: ex) 수입검사, 공정검사, 출하검사

    @Schema(description = "검사유형 [자동검사: AUTOMATIC_TEST, 수동검사: MANUAL_TEST, 검사없음: NO_TEST]")
    @NotNull(message = NOT_NULL)
    TestType testType;              // 검사유형: ex) 자동검사, 수동검사, 검사없음

    @Schema(description = "검사방법 [Sampling: SAMPLING, 전수: FULL_INSPECTION]")
    @NotNull(message = NOT_NULL)
    InspectionType inspectionType;  // 검사방법: ex) Sampling, 전수
}
