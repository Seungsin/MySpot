package com.myspot.myspot.user.service;


import com.myspot.myspot.user.domain.Role;
import com.myspot.myspot.user.domain.entity.UserEntity;
import com.myspot.myspot.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserSecurityService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityWrapper = userRepository.findByEmail(userEmail);

        if(userEntityWrapper.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        UserEntity userEntity = userEntityWrapper.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (("admin").equals(userEmail)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        return new User(userEntity.getEmail(), userEntity.getUserpassword(), authorities);
    }
}