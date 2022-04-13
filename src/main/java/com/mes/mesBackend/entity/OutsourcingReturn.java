package com.mes.mesBackend.entity;

import com.mes.mesBackend.dto.request.OutsourcingReturnRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "OUTSOURCING_RETURN")
@Data
public class OutsourcingReturn extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '외주반품 고유아이디'")
    private Long id;
    
    //LOT마스터
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOT_MASTER", columnDefinition = "bigint COMMENT 'LOT마스터'")
    private LotMaster lotMaster;

    //반품일시
    @Column(name = "RETURN_DATE", columnDefinition = "datetime(6) COMMENT '반품일시'")
    private LocalDate returnDate;

    //비고
    @Column(name = "NOTE", columnDefinition = "text COMMENT '비고'")
    private String note;

    @Column(name = "RETURN_DIVISION", columnDefinition = "bit(1) COMMENT '정상품/불량품 반품 유무'")
    private boolean returnDivision;

    //사용여부
    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;

    //삭제여부
    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;

    public void update(OutsourcingReturnRequest request, LotMaster lotMaster){
        setReturnDivision(request.isReturnDivision());
        setReturnDate(request.getReturnDate());
        setNote(request.getNote());
        setLotMaster(lotMaster);
    }

    public void delete(OutsourcingReturn returning){
        returning.setDeleteYn(true);
    }
}
