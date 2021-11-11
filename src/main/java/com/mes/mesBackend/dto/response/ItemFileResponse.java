package com.mes.mesBackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemFileResponse {
    Long id;
    String fileType;                   // 파일유형
    int version;;                     // VER
    String fileUrl;                  // 첨부파일유알엘
    String createdDate;              // 등록일자
    UserResponse.idAndKorName user; // 등록자
}
