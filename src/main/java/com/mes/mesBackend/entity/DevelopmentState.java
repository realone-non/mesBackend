package com.mes.mesBackend.entity;

import com.mes.mesBackend.dto.request.DevelopmentStateRequest;
import com.mes.mesBackend.entity.enumeration.DevelopmentChildrenStatusType;
import com.mes.mesBackend.entity.enumeration.DevelopmentStatusType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 개발등록 진행상태
 * 진행상태 (PLAN-개발의뢰서, Design-제품도면)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "DEVELOPMENT_STATES")
@Data
public class DevelopmentState extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '개발등록 진행상태 고유아이디'")
    private Long id;

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVELOPMENT", nullable = false, columnDefinition = "bigint COMMENT '개발품목 등록'")
    private Development development;       // 진행상태

    @Enumerated(EnumType.STRING)
    @Column(name = "DEVELOPMENT_STATUS", nullable = false, columnDefinition = "nvarchar(255) COMMENT '개발 프로세스(상위)'")
    private DevelopmentStatusType developmentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEVELOPMENT_CHILDREN_STATUS", nullable = false, columnDefinition = "nvarchar(255) COMMENT '개발 프로세스(하위)'")
    private DevelopmentChildrenStatusType developmentChildrenStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER", nullable = false, columnDefinition = "bigint COMMENT '등록자'")
    private User user;

    @Column(name = "FILE_NAME", columnDefinition = "nvarchar(500) COMMENT '파일 이름'")
    private String fileName;

    @Column(name = "FILE_URL", columnDefinition = "nvarchar(500) COMMENT '파일URL'")
    private String fileUrl;

    @Column(name = "CHANGE_CONTENTS", columnDefinition = "text COMMENT '변경 내용'")
    private String changeContents;

    @Column(name = "MEETING_TYPE", columnDefinition = "nvarchar(255) COMMENT '회의 구분'")
    private String meetingType;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @Column(name = "ORDERS", columnDefinition = "int COMMENT '순번'")
    private int orders;

    public void update(DevelopmentStateRequest request){
        setDevelopmentStatus(request.getDevelopmentStatus());
        setDevelopmentChildrenStatus(request.getDevelopmentChildrenStatus());
        setChangeContents(request.getChangeContents());
        setMeetingType(request.getMeetingType());
        setOrders(request.getOrders());
    }

    public void delete(){
        setDeleteYn(true);
    }

}
