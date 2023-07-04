package com.myspot.myspot.user.service;

import com.myspot.myspot.spot.domain.entity.LocationEntity;
import com.myspot.myspot.spot.domain.entity.SpotEntity;
import com.myspot.myspot.spot.domain.repository.LocationRepository;
import com.myspot.myspot.spot.domain.repository.SpotRepository;
import com.myspot.myspot.user.domain.Role;
import com.myspot.myspot.user.domain.entity.UserEntity;
import com.myspot.myspot.user.domain.repository.UserRepository;
import com.myspot.myspot.user.dto.MySpotListDto;
import com.myspot.myspot.user.dto.MySpotReviewDto;
import com.myspot.myspot.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private SpotRepository spotRepository;
    private LocationRepository locationRepository;

    //아이디 중복체크
    public Boolean checkUser(String userEmail){
        if(userRepository.findByEmail(userEmail).isEmpty()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //닉네임 중복체크
    public Boolean checkUsername(String userName){
        if(userRepository.findByusername(userName).isEmpty()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //회원가입
    @Transactional
    public Map<String, Object> joinUser(UserDto userDto) {
        Map<String, Object> result = new HashMap<>();
        if(!checkUser(userDto.getUser_email())) {
            result.put("result", "fail");
            return result;
        }
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword("{bcrypt}"+passwordEncoder.encode(userDto.getPassword()));

        result.put("user_email", userRepository.save(userDto.toEntity()).getEmail());
        result.put("result", "success");
        result.put("user_name", userDto.getUser_name());

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


    public Map<String, Object> getUserPage(String email) {
        Map<String, Object> result = new HashMap<>();

        List<SpotEntity> userSpotList = spotRepository.findAllByUseremail(email);

        List<MySpotListDto> mySpotListDtoList = new ArrayList<>();
        List<MySpotReviewDto> mySpotReviewDtoList = new ArrayList<>();

        for (SpotEntity getSpotEntity:userSpotList) {

            Optional<LocationEntity> locationInfoWrap = locationRepository.findById(getSpotEntity.getLocationnum());
            if(locationInfoWrap.isEmpty()){
                result.put("result", "fail");
                return result;
            }
            LocationEntity locationInfo = locationInfoWrap.get();
            //spot count 바꿔야함
            mySpotListDtoList.add(new MySpotListDto(getSpotEntity.getSpot_number(), locationInfo.getLocationname(), locationInfo.getLocationAddress(), locationInfo.getLocationLatitude(), locationInfo.getLocationLongitude(), 1));

            //이미지 링크 걸어야함 -> 해결
            if(getSpotEntity.getSpot_Photo()==null){
                mySpotReviewDtoList.add(new MySpotReviewDto(null, getSpotEntity.getSpot_Comment(), getSpotEntity.getSpot_date()));
            }else{
                String baseLink = "https://storage.googleapis.com/myspot_photo_storage/spotImg/";

                String spotPhotoString = getSpotEntity.getSpot_Photo();
                spotPhotoString = spotPhotoString.substring(1, spotPhotoString.length()-1);
                List<String> spotPhotoList = new ArrayList<>(List.of(spotPhotoString.split(", ")));

                for(int i=0;i<spotPhotoList.size();i++){
                    spotPhotoList.set(i, baseLink+spotPhotoList.get(i));
                }

                mySpotReviewDtoList.add(new MySpotReviewDto(spotPhotoList, getSpotEntity.getSpot_Comment(), getSpotEntity.getSpot_date()));
            }
        }

        result.put("result", "success");
        result.put("mySpotList", mySpotListDtoList);
        result.put("mySpotReviewList", mySpotReviewDtoList);

        return result;
    }
}