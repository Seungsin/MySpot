package com.myspot.myspot.user.dto;

import com.myspot.myspot.user.domain.entity.UserEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {

    private static final String ROLE_PREFIX = "MEMBER";
    private String user_email;
    private String password;
    private String user_name;
    private Boolean user_social;
    private LocalDateTime user_joindate;
    private LocalDateTime user_lastdate;
    private String user_image;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        String role = "MEMBER";
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
        return authorities;
    }

    public UserEntity toEntity(){
        return UserEntity.builder()
                .email(user_email)
                .password(password)
                .social(user_social)
                .name(user_name)
                .build();
    }

    @Builder
    public UserDto(String email, String password, Boolean social, String name) {
        this.user_email = email;
        this.password = password;
        this.user_social = social;
        this.user_name = name;
    }


}