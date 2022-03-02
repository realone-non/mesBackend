package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.ModifiedDivision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "MODIFIED_LOGS")
@Data
public class ModifiedLog {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '업데이트 로그 고유아이디'")
    private Long id;

    @Column(name = "USER_CODE", columnDefinition = "varchar(255) COMMENT '사번'", nullable = false)
    private String userCode;

    @Enumerated(STRING)
    @Column(name = "MODIFIED_DIVISION", columnDefinition = "varchar(255) COMMENT '구분'", nullable = false)
    private ModifiedDivision modifiedDivision;

    @Column(name = "MODIFIED_DATE", columnDefinition = "datetime COMMENT '수정일'", nullable = false)
    private LocalDateTime modifiedDate;

    @Column(name = "USER_LEVEL", columnDefinition = "int COMMENT '유저권한레벨'", nullable = false)
    private int userLevel;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "UNIT", columnDefinition = "bigint COMMENT '단위'")
    private Unit unit;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM_GROUP", columnDefinition = "bigint COMMENT '품목그룹'")
    private ItemGroup itemGroup;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_PROCESS", columnDefinition = "bigint COMMENT '작업공정'")
    private WorkProcess workProcess;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_LINE", columnDefinition = "bigint COMMENT '작업라인'")
    private WorkLine workLine;

    public <T> ModifiedLog created(String userCode, ModifiedDivision modifiedDivision, int userLevel, T t) {
        setUserCode(userCode);
        setModifiedDate(LocalDateTime.now());
        setModifiedDivision(modifiedDivision);
        setUserLevel(userLevel);

        switch (modifiedDivision) {
            case UNIT: setUnit(t);
                break;
            case ITEM_GROUP: setItemGroup(t);
                break;
            case WORK_PROCESS: setWorkProcess(t);
            break;
            case WORK_LINE: setWorkLine(t);
        }

        return this;
    }

    public <T> void setUnit(T t) {
        this.unit = (Unit) t;
    }
    public <T> void setItemGroup(T t) {
        this.itemGroup = (ItemGroup) t;
    }
    public <T> void setWorkProcess(T t) {
        this.workProcess = (WorkProcess) t;
    }
    public <T> void setWorkLine(T t) {
        this.workLine = (WorkLine) t;
    }
}
