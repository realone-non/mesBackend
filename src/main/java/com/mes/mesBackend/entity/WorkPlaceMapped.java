package com.mes.mesBackend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 사업장 & 업태 매핑 테이블
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Entity(name = "WORK_PLACE_AND_BUSINESS_TYPE_MAPPEDS")
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "ID")
public class WorkPlaceMapped extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

//    /insertable = false, updatable = false
    @ManyToOne @JoinColumn(name = "WORK_PLACES_ID")
    private WorkPlace workPlace;

    @ManyToOne @JoinColumn(name = "BUSINESS_TYPES_ID")
    private BusinessType businessType;
}
