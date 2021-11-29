package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "USER_ROLES")
@Data
public class UserRole extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "bigint COMMENT '고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER", columnDefinition = "bigint COMMENT '직원 고유아이디'")
    private User user;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ROLE", columnDefinition = "bigint COMMENT '권한'")
    private Role role;

    public UserRole save(User user, Role role) {
        this.setUser(user);
        this.setRole(role);
        return this;
    }
}
