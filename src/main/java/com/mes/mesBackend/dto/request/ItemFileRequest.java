package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.*;

@Getter
@Setter
public class ItemFileRequest {
    @Min(value = ID_VALUE, message = NOT_ZERO)
    int version;           // VER

    @Min(value = ID_VALUE, message = NOT_ZERO)
    @NotNull(message = NOT_NULL)
    Long user; // 등록자
}
