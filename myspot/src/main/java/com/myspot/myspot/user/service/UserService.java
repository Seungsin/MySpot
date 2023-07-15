package com.myspot.myspot.user.service;

import com.myspot.myspot.user.domain.entity.UserEntity;
import com.myspot.myspot.user.domain.repository.UserRepository;
import com.myspot.myspot.user.dto.UserRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    //아이디 중복체크
    public Boolean checkUser(String userEmail){
        if(userRepository.findByEmail(userEmail).isEmpty()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //회원가입
    @Transactional
    public Map<String, Object> joinUser(UserRequestDto userRequestDto) {
        Map<String, Object> result = new HashMap<>();
        if(!checkUser(userRequestDto.getUser_email())) {
            result.put("result", "fail");
            return result;
        }
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userRequestDto.setPassword("{bcrypt}"+passwordEncoder.encode(userRequestDto.getPassword()));

        result.put("user_email", userRepository.save(userRequestDto.toEntity()).getEmail());
        result.put("result", "success");
        result.put("user_name", userRequestDto.getUser_name());

        return result;
    }

    //회원 정보
    public Map<String, Object> getUserInfo(String userEmail){
        Map<String, Object> result = new HashMap<>();
        Optional<UserEntity> userInfoWrap = userRepository.findByEmail(userEmail);
        if(userInfoWrap.isEmpty()){
            result.put("result", "error");
            result.put("message", "일치하는 회원 정보가 없습니다.");
            return result;
        }

        UserEntity userInfo = userInfoWrap.get();

        result.put("result", "success");
        result.put("user_email", userInfo.getEmail());
        result.put("user_name", userInfo.getUsername());

        return result;
    }
}