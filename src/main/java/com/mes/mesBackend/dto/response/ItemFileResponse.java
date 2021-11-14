package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@Schema(description = "품목파일")
public class ItemFileResponse {
    @Schema(description = "고유아이디")
    Long id;

    @Schema(description = "파일유형")
    String fileType;                   // 파일유형

    @Schema(description = "VER")
    int version;                     // VER

    @Schema(description = "첨부파일유알엘")
    String fileUrl;                  // 첨부파일유알엘

    @Schema(description = "등록일자")
    String createdDate;              // 등록일자

    @Schema(description = "등록자")
    UserResponse.idAndKorName user; // 등록자
}
