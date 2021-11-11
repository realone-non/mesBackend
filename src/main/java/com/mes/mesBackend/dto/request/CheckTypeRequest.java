package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

// 점검유형
@Getter
@Setter
public class CheckTypeRequest {
    @NotBlank(message = NOT_NULL)
    String checkType;       // 점검유형
}
