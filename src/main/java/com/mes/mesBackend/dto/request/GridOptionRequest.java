package com.mes.mesBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "그리드 옵션")
public class GridOptionRequest {
    String aggFunc;
//    String colId;
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
