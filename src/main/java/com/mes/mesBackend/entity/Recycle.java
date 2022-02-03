package com.mes.mesBackend.entity;


import com.mes.mesBackend.dto.request.RecycleRequest;
import com.mes.mesBackend.entity.enumeration.RecycleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 재사용 유형
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "RECYCLES")
@Data
public class Recycle extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '재사용 유형 고유번호'" ,nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_PROCESS", columnDefinition = "bigint COMMENT '공정'", nullable = false)
    private WorkProcess workProcess;

    @Column(name = "RECYCLE_NAME", columnDefinition = "nvarchar(255) COMMENT '재사용 유형'")
    private String recycleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "RECYCLE_TYPE", columnDefinition = "nvarchar(255) COMMENT '재사용 타입'")
    private RecycleType recycleType;

    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용 여부'")
    private boolean useYn = true;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제 여부'")
    private boolean deleteYn = false;

    public void update(RecycleRequest request){
        setRecycleName(request.getRecycleName());
        setRecycleType(request.getRecycleType());
        setUseYn(request.isUseYn());
    }

    public void delete(){
        setDeleteYn(true);
    }
}
