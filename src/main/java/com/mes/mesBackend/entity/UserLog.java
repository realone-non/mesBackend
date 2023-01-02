package main.java.com.mes.mesBackend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

// 유저 로그인 Log
@NoArgsConstructor
@Entity(name = "USER_LOGS")
@Data
public class UserLog extends com.mes.mesBackend.entity.BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '유저 로그 고유 아이디'")
    private Long id;

    @Column(name = "USER_IP", columnDefinition = "varchar(500) COMMENT '유저 IP'")
    private String userIp;

    @Column(name = "USE_METHOD", columnDefinition = "varchar(500) COMMENT '사용 구분'")
    private String useMethod;

    @Column(name = "UserId", columnDefinition = "varchar(500) COMMENT '유저 ID'")
    private String userId;

    @Column(name = "RESULT_CODE", columnDefinition = "varchar(500) COMMENT '수신 결과 코드'")
    private String ResultCode;

    @Column(name = "RESULT_MSG", columnDefinition = "varchar(500) COMMENT '수신 결과 메시지'")
    private String ResultMsg;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부
}
