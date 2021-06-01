package com.mes.mesBackend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="COM_COMPANY")
@Data
public class CompanyVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID companyId;

    @Column (nullable = false)
    private String factoryCode;
    @Column (nullable = false)
    private String companyName;
    @Column(nullable = false)
    private String companyType;
    private String companyNumber;
    private String chargerName;
    private String cellphoneNumber;
    private String companyEmail;
    private String companyTelNumber;
    private String companyAddress;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "companyImage", columnDefinition = "LONGBLOB")
    private byte[] companyImage;
    private boolean isUse;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdDate;
    private String createId;
    @Column(nullable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedDate;
    private String modifyId;
}
