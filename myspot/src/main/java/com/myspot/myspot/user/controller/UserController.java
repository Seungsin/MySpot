package com.myspot.myspot.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.myspot.myspot.user.dto.UserDto;
import com.myspot.myspot.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.ldap.HasControls;
import javax.swing.text.StyledEditorKit;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    // 회원가입
    @PostMapping("/user/signup")
    public ResponseEntity<Map<String, Object>> execSignup(@RequestBody UserDto userDto) {
        Map<String, Object> result = userService.joinUser(userDto);
        if(result.get("result").equals("fail")){
            return ResponseEntity.badRequest().body(result);
        }else{
            return ResponseEntity.ok().body(result);
        }
    }

    //로그인 성공
    @RequestMapping("/user/login/{email}")
    public ResponseEntity<Map<String, Object>> successLogin(@PathVariable("email") String email){
        Map<String, Object> result = userService.getUserInfo(email);

        if(result.get("result").equals("fail")){
            return ResponseEntity.badRequest().body(result);
        }else {
            return ResponseEntity.ok().body(result);
        }
    }

    //이메일 중복체크
    @GetMapping("/user/checkEmail")
    public ResponseEntity checkEmail(@RequestParam("email") String email){
        if(userService.checkUser(email)){
            return ResponseEntity.ok().body("");
        }else{
            return ResponseEntity.badRequest().body("");
        }
    }

    //닉네임 중복체크
    @GetMapping("/user/checkName")
    public ResponseEntity checkUserName(@RequestParam("name") String name){
        if(userService.checkUsername(name)){
            return ResponseEntity.ok().body("");
        }else{
            return ResponseEntity.badRequest().body("");
        }
    }

    // 내 정보 페이지
    @GetMapping("/user/info")
    public ResponseEntity<Map<String, Object>> dispMyInfo(@RequestParam("email") String email) {
        Map<String, Object> responseBody = userService.getUserPage(email);

        if(responseBody.get("result").equals("fail")){
            return ResponseEntity.badRequest().body(responseBody);
        }

        return ResponseEntity.ok().body(responseBody);
    }

    // 프로필사진 등록
    @PostMapping("/user/upload/profile")
    public ResponseEntity<Map<String, Object>> profilePhotoUpload(MultipartFile uploadProfilePhoto, String email) throws IOException {
        Map<String, Object> responseBody = userService.uploadUserPhoto(uploadProfilePhoto, email);

        if(responseBody.get("result").equals("fail")){
            return ResponseEntity.badRequest().body(responseBody);
        }

        return ResponseEntity.ok().body(responseBody);
    }

    //유저 마이페이지
//    @RequestMapping("/user/info/mypage")
//    public ResponseEntity<Map<String, Object>> dispMypage(){
//
//    }
}
