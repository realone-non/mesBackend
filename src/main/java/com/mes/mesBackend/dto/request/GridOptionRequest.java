package com.mes.mesBackend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GridOptionRequest {
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
