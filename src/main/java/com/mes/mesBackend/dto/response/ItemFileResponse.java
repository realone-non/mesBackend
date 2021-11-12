package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
@ApiModel(description = "품목파일")
public class ItemFileResponse {
    @ApiModelProperty(value = "고유아이디")
    Long id;

    @ApiModelProperty(value = "파일유형")
    String fileType;                   // 파일유형

    @ApiModelProperty(value = "VER")
    int version;;                     // VER

    @ApiModelProperty(value = "첨부파일유알엘")
    String fileUrl;                  // 첨부파일유알엘

    @ApiModelProperty(value = "등록일자")
    String createdDate;              // 등록일자

    @ApiModelProperty(value = "등록자")
    UserResponse.idAndKorName user; // 등록자
}
