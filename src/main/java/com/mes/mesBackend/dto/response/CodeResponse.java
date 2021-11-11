package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class CodeResponse {
    Long id;
    String code;
    String codeName;
    String description;
    boolean useYn;

    @Getter
    @Setter
    public static class idAndCode {
        Long id;
        String code;
    }
}
