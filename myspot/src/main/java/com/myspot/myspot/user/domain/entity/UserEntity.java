package com.myspot.myspot.user.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
public class UserEntity {
    @Id
    @Column(length = 20, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String userpassword;

    @Column(length = 20, nullable = false)
    private String username;

    private Boolean usersocial;

    private int userlevel;

    private LocalDateTime userjoindate;

    private LocalDateTime  userlastdate;

    private String userimage;

    @Builder
    public UserEntity(String email, String password, Boolean social, String name) {
        this.email = email;
        this.userpassword = password;
        this.usersocial = Boolean.TRUE;
        this.userjoindate = LocalDateTime.now();
        this.userlastdate = LocalDateTime.now();
        this.userlevel = 0;
        this.userimage = "none";
        this.username = name;
    }
}
