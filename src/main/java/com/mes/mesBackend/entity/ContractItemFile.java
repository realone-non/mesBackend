package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// 수주 품목 파일 리스트
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "CONTRACT_ITEM_FILES")
@Data
public class ContractItemFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '수주 품목 첨부파일 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CONTRACT_ITEM", columnDefinition = "bigint COMMENT '수주 품목 고유아이디'", nullable = false)
    private ContractItem contractItem;

    @Column(name = "FILE_URL", columnDefinition = "varchar(500) COMMENT '파일 url'", nullable = false)
    private String fileUrl;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;

    public void add(ContractItem contractItem, String fileUrl) {
        setContractItem(contractItem);
        setFileUrl(fileUrl);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
