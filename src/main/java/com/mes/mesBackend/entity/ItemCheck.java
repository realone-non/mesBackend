package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.TestCategory;
import com.mes.mesBackend.entity.enumeration.TestType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
 * 3-4-2. 품목별 검사항목 등록
 * 검색: 공장,검사유형,품목그룹,품목계정
 * 품번
 * 품명
 * 검사유형
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "ITEM_CHECKS")
@Data
public class ItemCheck extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목별 검사항목 등록 고유아이디'")
    private Long id;

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'", nullable = false)
    private Item item;      // 품번, 품명

    @Enumerated(EnumType.STRING)
    @Column(name = "CHECK_CATEGORY", columnDefinition = "varchar(255) COMMENT '검사유형'", nullable = false)
    private TestCategory checkCategory = TestCategory.INPUT_TEST;    // 검사유형

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    public void addJoin(Item item) {
        setItem(item);
    }

    public void update(ItemCheck newItemCheck, Item newItem) {
        setItem(newItem);
        setCheckCategory(newItemCheck.checkCategory);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
