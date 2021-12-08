package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "수주 품목 파일")
public class ContractItemFileResponse {
    @Schema(description = "고유아이디")
    Long id;
    @Schema(description = "파일 url")
    String fileUrl;
}
