package com.mes.mesBackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class GridOptionResponse {
    String aggFunc;
    String colId;
    String flex;
    boolean hide;
    String pinned;
    boolean pivot;
    String pivotIndex;
    boolean rowGroup;
    String rowGroupIndex;
    String sort;
    String sortIndex;
    String width;
}
