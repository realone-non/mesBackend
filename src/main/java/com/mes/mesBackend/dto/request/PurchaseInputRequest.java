package com.mes.mesBackend.dto.request;

import com.mes.mesBackend.entity.LotType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
@Schema(description = "구매입고")
public class PurchaseInputRequest {
    @Schema(description = "입고수량")
    @Min(value = ONE_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    int inputAmount;

    @Schema(description = "제조일자")
    LocalDate manufactureDate;

    @Schema(description = "유효일자")
    @NotNull(message = NOT_NULL)
    LocalDate validDate;

    @Schema(description = "긴급여부")
    @NotNull(message = NOT_NULL)
    boolean urgentYn;

    @Schema(description = "시험성적서")
    @NotNull(message = NOT_NULL)
    boolean testReportYn;

    @Schema(description = "COC")
    @NotNull(message = NOT_NULL)
    boolean coc;

    // lot 관련
    Long lotType;
    boolean processYn;

    @Getter
    @Setter
    @Schema(description = "구매입고 수정")
    public static class updateRequest {
        @Schema(description = "입고수량")
        @Min(value = ONE_VALUE, message = NOT_ZERO)
        @NotNull(message = NOT_NULL)
        int inputAmount;

        @Schema(description = "제조일자")
        LocalDate manufactureDate;

        @Schema(description = "유효일자")
        @NotNull(message = NOT_NULL)
        LocalDate validDate;

        @Schema(description = "긴급여부")
        @NotNull(message = NOT_NULL)
        boolean urgentYn;

        @Schema(description = "시험성적서")
        @NotNull(message = NOT_NULL)
        boolean testReportYn;

        @Schema(description = "COC")
        @NotNull(message = NOT_NULL)
        boolean coc;
    }
}
