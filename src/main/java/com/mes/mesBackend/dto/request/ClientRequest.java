package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.enumeration.InspectionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "거래처")
public class ClientRequest {

    @Schema(description = "거래처코드")
    @NotBlank(message = NOT_NULL)
    String clientCode;

    @Schema(description = "거래처명")
    @NotBlank(message = NOT_NULL)
    String clientName;

    @Schema(description = "약어")
    String shortName;

    @Schema(description = "거래처 유형 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long clientType;

    @Schema(description = "사업자등록번호")
    @NotBlank(message = NOT_NULL)
    String businessNumber;

    @Schema(description = "사업자등록증")
    String businessFile;

    @Schema(description = "대표자명")
    @NotBlank(message = NOT_NULL)
    String ceoName;

    @Schema(description = "우편번호")
    String postalCode;

    @Schema(description = "주소")
    String address;

    @Schema(description = "상세주소")
    String detailAddress;

    @Schema(description = "업태 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long businessType;

    @Schema(description = "업종")
    String item;

    @Schema(description = "전화번호")
    String telNumber;

    @Schema(description = "fax번호")
    String faxNumber;

    @Schema(description = "홈페이지")
    String homePageUrl;

    @Schema(description = "메일")
    String mail;

    @Schema(description = "담당자 이름")
    String clientChargeName;

    @Schema(description = "무역업등록번호")
    String tradeBusinessRegNo;

    @Schema(description = "통관고유번호")
    String pccc;

    @Schema(description = "국가코드 id")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    Long countryCode;

    @Schema(description = "지역")
    String area;

    @Schema(description = "거래화폐단위")
    String currencyUnit;

    @Schema(description = "회사담당자")
    String companyCharge;

    @Schema(description = "회사담당부서")
    String companyChargeDept;

    @Schema(description = "대금결제방법")
    String paymentMethod;

    @Schema(description = "결제일자")
    String paymentDate;

    @Schema(description = "운송방법")
    String transitMethod;

    @Schema(description = "검사방법")
    InspectionType inspectionType;

    @Schema(description = "검색어")
    String searchWord;

    @Schema(description = "휴대폰번호")
    String phoneNumber;

    @Schema(description = "사용여부")
    @NotNull(message = NOT_NULL)
    boolean useYn;
}
