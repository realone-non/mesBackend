package com.mes.mesBackend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
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
    @Type(type="uuid-char")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "COMPANY_ID", columnDefinition = "VARCHAR(36)")
    private UUID companyId;

    @Column (nullable = false, name = "FACTORY_CODE")
    private String factoryCode;
    @Column (nullable = false, name = "COMPANY_NAME")
    private String companyName;
    @Column (nullable = false, name = "COMPANY_TYPE")
    private String companyType;
    @Column (name = "COMPANY_NUMBER")
    private String companyNumber;
    @Column (name = "CHARGER")
    private String chargerName;
    @Column (name = "PHONE_NUMBER")
    private String cellphoneNumber;
    @Column (name = "EMAIL")
    private String companyEmail;
    @Column (name = "COMPANY_TEL")
    private String companyTelNumber;
    @Column (name = "COMPANY_ADDRESS")
    private String companyAddress;
    @Column (name = "IS_USE")
    private boolean useYn = true;

    @Column(nullable = false, updatable = false, name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdDate;
    @Column(name = "CREATE_ID")
    private String createId;
    @Column(nullable = false, updatable = true, name = "MODIFY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modifiedDate;
    @Column(name = "MODIFY_ID")
    private String modifyId;
}
