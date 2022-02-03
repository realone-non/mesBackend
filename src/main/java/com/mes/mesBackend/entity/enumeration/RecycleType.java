package com.mes.mesBackend.entity.enumeration;

public enum RecycleType {
    /*
     * 재사용 유형
     *  PRINT_BAD : 인쇄불량
     *  LABEL_TWISTED : 라벨 틀어짐(부착)
     *  TAPE_BAD : 투명테이프 부착불량
     *  TAPE_FOREIGN : 테이프 이물질
     *  CUTTING_BAD : 컷팅 불량
     *  BOX_BOND_BAD : 단상자 접착 불량
     *  BOX_COLOR_BAD : 단상자 이염
     *  BOX_PRINT_BAD : 단상자 인쇄불량
     *  THREE_PACK_BOND_BAD : 3팩 접착불량
     *  THREE_PACK_COLOR_BAD : 3팩 이염
     *  THREE_PACK_PRINT_BAD : 3팩 인쇄불량
     *  CARTON_BOX_BOND_BAD : 카톤박스 접착불량
     *  CARTON_BOX_PRINT_BAD : 카톤박스 인쇄불량
     *  SUB_MATERIAL_TAPE_BAD : 부자재불량(양면테이프)
     *  SUB_MATERIAL_CAP : 부자재불량(흡착캡)
     */
    PRINT_BAD, LABEL_TWISTED, TAPE_BAD, TAPE_FOREIGN, CUTTING_BAD, BOX_BOND_BAD, BOX_COLOR_BAD, BOX_PRINT_BAD,
    THREE_PACK_BOND_BAD, THREE_PACK_COLOR_BAD, THREE_PACK_PRINT_BAD, CARTON_BOX_BOND_BAD, CARTON_BOX_PRINT_BAD,
    SUB_MATERIAL_TAPE_BAD, SUB_MATERIAL_CAP
}
