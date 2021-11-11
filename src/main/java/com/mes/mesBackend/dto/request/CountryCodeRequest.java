package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mes.mesBackend.exception.Message.NOT_NULL;

@Getter
@Setter
public class CountryCodeRequest {
    @NotBlank(message = NOT_NULL)
    String name;
    @NotNull(message = NOT_NULL)
    boolean useYn;  // 사용여부
}
