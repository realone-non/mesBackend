package com.mes.mesBackend.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="COM_COMPANIES")
@Data
public class CompanyVo extends BaseTimeEntity{
    @Id
    @Type(type="uuid-char")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)", name = "ID")
    private UUID id;

    @Column (nullable = false, name = "FACTORY_CODE")
    private String factoryCode;

    @Column (nullable = false, name = "COMPANY_NAME")
    private String companyName;

    @Column (nullable = false, name = "COMPANY_TYPE")
    private String companyType;

    @Column (name = "COMPANY_NUMBER")
    private String companyNumber;

    @Column(name = "CHARGER_NAME")
    private String chargerName;

    @Column(name = "CELLPHONE_NUMBER")
    private String cellphoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TEL_NUMBER")
    private String telNumber;

    @Column(name = "ADDRESS")
    private String Address;

    @Column(name = "USE_YN")
    private boolean useYn = true;

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}
