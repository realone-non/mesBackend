package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mes.mesBackend.entity.enumeration.OrderState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Schema(description = "pop-출하")
@JsonInclude(NON_NULL)
public class LabelShipmentResponse {
    // 출하 id
    @Schema(description = "출하 고유아이디")
    Long shipmentId;

    // 출하 바코드
    @Schema(description = "출하 바코드")
    String barcodeNumber;

    // 거래처 명
    @Schema(description = "거래처 명")
    String clientName;

    // 제조일자1
    @Schema(description = "제조일자 1")
    LocalDate createdDate1;

    // 제조일자2
    @Schema(description = "제조일자 2")
    LocalDate createdDate2;

    // 품번 여러개
    @Schema(description = "품번")
    List<String> itemNo;

    // 품명 여러개
    @Schema(description = "품명")
    List<String> itemName;

    // 수량 해당되는 수량 모두
    @Schema(description = "출하 수량")
    int shipmentAmount;

    // 상태값
    @Schema(description = "상태값")
    OrderState orderState;

    @Schema(description = "라벨프린트 출력 여부")
    boolean labelPrintYn;
}
