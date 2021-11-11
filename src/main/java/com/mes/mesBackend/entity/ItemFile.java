package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 품목등록 파일
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ITEM_FILES")
@Data
public class ItemFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '품목 파일 고유아이디'")
    private Long id;

    @Column(name = "FILE_TYPE", columnDefinition = "varchar(255) COMMENT '파일유형'")
    private String fileType;        // 파일유형

    @Column(name = "VERSION", columnDefinition = "int COMMENT '파일버전'")
    private int version;            // VER

    @Column(name = "FILE_URL",length = 400, columnDefinition = "varchar(255) COMMENT '첨부파일 url'")
    private String fileUrl;     // 첨부파일유알엘

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER", columnDefinition = "bigint COMMENT '등록자'", nullable = false)
    private User user;      // 등록자

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'")
    private Item item;

    public void addFileUrl(String fileUrl, String fileType) {
        setFileUrl(fileUrl);
        setFileType(fileType);
    }

    public void update(ItemFile newItemFile, User newUser) {
        setVersion(newItemFile.version);
        setUser(newUser);
    }

    public void add(User user, Item item) {
        setUser(user);
        setItem(item);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
