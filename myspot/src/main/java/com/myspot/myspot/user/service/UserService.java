package com.myspot.myspot.user.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.myspot.myspot.spot.domain.entity.LocationEntity;
import com.myspot.myspot.spot.domain.entity.SpotEntity;
import com.myspot.myspot.spot.domain.repository.LocationRepository;
import com.myspot.myspot.spot.domain.repository.SpotRepository;
import com.myspot.myspot.user.domain.entity.UserEntity;
import com.myspot.myspot.user.domain.repository.UserRepository;
import com.myspot.myspot.user.dto.MySpotListDto;
import com.myspot.myspot.user.dto.MySpotReviewDto;
import com.myspot.myspot.user.dto.UserRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private SpotRepository spotRepository;
    private LocationRepository locationRepository;
    private final Storage storage;

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
                //String baseLink = "https://storage.googleapis.com/myspot_photo_storage/spotImg/";

                String spotPhotoString = getSpotEntity.getSpot_Photo();
                spotPhotoString = spotPhotoString.substring(1, spotPhotoString.length()-1);
                List<String> spotPhotoList = new ArrayList<>(List.of(spotPhotoString.split(", ")));

                mySpotReviewDtoList.add(new MySpotReviewDto(spotPhotoList, getSpotEntity.getSpot_Comment(), getSpotEntity.getSpot_date()));
            }
        }

        result.put("result", "success");
        result.put("mySpotList", mySpotListDtoList);
        result.put("mySpotReviewList", mySpotReviewDtoList);

        return result;
    }

    //프로필 등록
    public Map<String, Object> uploadUserPhoto(MultipartFile uploadPhoto, String email) throws IOException {
        Map<String, Object> result = new HashMap<>();

        String userProfilePhoto = "";

        //이미지가 존재할때
        if (uploadPhoto!=null) {
            String baseLink = "https://storage.googleapis.com/myspot_photo_storage/userProfilePhoto/";

            //GCP에 이미지 등록
            String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름

            String ext = uploadPhoto.getContentType(); // 파일의 형식 ex) JPG

            if(ext==null||!ext.contains("image")){
                result.put("result", "fail");
                result.put("message", "사진이 없거나 올바른 형태가 아닙니다.");
                return result;
            }

            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder("myspot_photo_storage", "userProfilePhoto/" + uuid)
                            .setContentType(ext)
                            .build(),
                    uploadPhoto.getInputStream());

            userProfilePhoto = baseLink+uuid;
        }

        //DB에 저장
        userRepository.findById(email).orElseThrow(()->new IllegalArgumentException("해당 계정이 없습니다.")).update(userProfilePhoto);

        Map<String, String> resultBody = new HashMap<>();
        resultBody.put("email", email);
        resultBody.put("photoURL", userProfilePhoto);

        result.put("result", "success");
        result.put("message", resultBody);

        return result;
    }
}