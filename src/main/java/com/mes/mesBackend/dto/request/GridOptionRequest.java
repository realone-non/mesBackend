package com.mes.mesBackend.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "그리드 옵션")
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
