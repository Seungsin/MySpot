package com.myspot.myspot.spot.service;

import com.google.api.client.util.DateTime;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.myspot.myspot.spot.domain.entity.LocationEntity;
import com.myspot.myspot.spot.domain.entity.SpotEntity;
import com.myspot.myspot.spot.domain.repository.LocationRepository;
import com.myspot.myspot.spot.domain.repository.SpotRepository;
import com.myspot.myspot.spot.domain.repository.SpotTagRepository;
import com.myspot.myspot.spot.dto.*;
import com.myspot.myspot.user.domain.entity.UserEntity;
import com.myspot.myspot.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class SpotService {
    private SpotRepository spotRepository;
    private UserRepository userRepository;
    private LocationRepository locationRepository;
    private LocationService locationService;
    private SpotTagRepository spotTagRepository;
    private final Storage storage;
    //spot 등록
    @Transactional
    public Map<String, Object> addNewSpot(AddSpotDTO addspotDto) throws IOException {
        Map<String, Object> result = new HashMap<>();
        //System.out.println(spotDto);

        if(addspotDto==null){
            result.put("result", "fail");
        }else {
            if (userRepository.findByEmail(addspotDto.getUser_email()).isEmpty()) {
                result.put("result", "fail");
                result.put("message", "유저 정보가 없습니다.");
                result.put("email", addspotDto.getUser_email());

                return result;
            } else if (addspotDto.getLocationLatitude().isEmpty() || addspotDto.getLocationLongitude().isEmpty() || addspotDto.getLocationMapCode().isEmpty()) {
                result.put("result", "fail");
                result.put("message", "장소 정보가 정확하지 않습니다.");

                return result;
            }
            if (locationRepository.findByLocationmapcode(addspotDto.getLocationMapCode()).isEmpty()) {
                locationService.addLocationInfo(new LocationDTO(addspotDto.getLocationName(), addspotDto.getLocationMapCode(), addspotDto.getLocationLatitude(), addspotDto.getLocationLongitude(), addspotDto.getLocationMapCode(), addspotDto.getLocationAddress()));
            }
            Date now = new Date();
            Timestamp timestamp = new Timestamp(now.getTime());

            SpotDto spotDto = new SpotDto(addspotDto.getUser_email(), Integer.valueOf(addspotDto.getLocationMapCode()), addspotDto.getLocationName(), addspotDto.getSpotCategory(), addspotDto.getSpotComment(), null, addspotDto.getSpotFolder(), now);

            //이미지가 존재할때
            if (addspotDto.getSpotImg()!=null) {
                List<String> imgNames = new ArrayList<>();
                //GCP에 이미지 등록
                for (MultipartFile spotImg : addspotDto.getSpotImg()) {
                    String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름

                    String ext = spotImg.getContentType(); // 파일의 형식 ex) JPG

                    BlobInfo blobInfo = storage.create(
                            BlobInfo.newBuilder("myspot_photo_storage", "spotImg/" + uuid)
                                    .setContentType(ext)
                                    .build(),
                            spotImg.getInputStream());
                    imgNames.add(uuid);
                }

                //DB에 저장
                spotDto.setSpotPhoto(imgNames.toString());
            }

            SpotEntity spotEntity = spotDto.toEntity();

            result.put("result", "success");
            result.put("message", spotEntity);
            spotRepository.save(spotEntity);


            for (String tag : addspotDto.getSpotTag()) {
                SpotTagDTO spotTagDTO = new SpotTagDTO(Integer.valueOf(addspotDto.getLocationMapCode()), spotEntity.getSpot_number(), tag);
                spotTagRepository.save(spotTagDTO.toEntity());
            }

        }
        return result;
    }

    //스팟 삭제
    public Map<String, Object> deleteSpot(String email, int spotNum){
        Map<String, Object> result = new HashMap<>();

        Optional<SpotEntity> spotInfoWrap = spotRepository.findById(spotNum);

        if(spotInfoWrap.isEmpty()) {
            result.put("result", "fail");
            result.put("message", "해당 스팟 정보가 존재하지 않습니다");
            return result;
        }else if(!spotInfoWrap.get().getUseremail().equals(email)){
            result.put("result", "fail");
            result.put("message", "유저 정보가 일치하지 않습니다.");
            return result;
        }
        spotRepository.delete(spotRepository.findById(spotNum).get());
        result.put("result", "success");
        result.put("message", spotNum+" 삭제완료");

        return result;
    }

    //유저의 스팟 리스트 조회???
    public Map<String, Object> findUserSpot(String userEmail) {
        Map<String, Object> result = new HashMap<>();

        Optional<UserEntity> userInfoWrap = userRepository.findByEmail(userEmail);
        if(userInfoWrap.isEmpty()){
            result.put("result", "회원 정보가 없습니다.");
            return result;
        }else{
            List<SpotEntity> spotInfoList = spotRepository.findAllByUseremail(userEmail);
            result.put("result", "success");
            result.put("data", spotInfoList);
        }

        return result;
    }


    //스팟 리스트 검색
    public Map<String, Object> getSpotList(String searchWord,String category,String keyword,Double lat,Double lon) {
        Map<String, Object> result = new HashMap<>();

        List<LocationEntity> locationList = locationRepository.findAll();
        if(locationList.isEmpty()){
            result.put("result", "fail");
            result.put("message", "근처 가게 정보가 없습니다.");
            return result;
        }

        List<SearchResultDTO> resultList = new ArrayList<>();
        for(int i=0;i<locationList.size();i++){
            LocationEntity getOne = locationList.get(i);
            double dist = distance(lat, lon, Double.parseDouble(getOne.getLocationLatitude()), Double.parseDouble(getOne.getLocationLongitude()), "meter");
            if(dist<500000000){
                int count = spotRepository.countByLocationnum(getOne.getLocation_num()).intValue();

                resultList.add(new SearchResultDTO(getOne.getLocation_num().toString(), getOne.getLocationAddress(), getOne.getLocationLongitude(), getOne.getLocationLatitude(), getOne.getLocationname(), count));
            }
        }

        if(resultList.isEmpty()){
            result.put("result", "fail");
            result.put("message", "근처 가게 정보가 없습니다.");

            return result;
        }


        result.put("result", "success");
        result.put("data", resultList);


        return result;
    }

    //상세 리뷰 받기
    public Map<String, Object> getSpotReviews(String locationCode) {
        Map<String, Object> result = new HashMap<>();

        Optional<LocationEntity> locationWraper = locationRepository.findByLocationmapcode(locationCode);
        if(locationWraper.isEmpty()){
            result.put("result", "fail");
            result.put("message", "장소 정보가 없습니다.");
            return result;
        }

        Integer locationNum = locationWraper.get().getLocation_num();

        List<SpotEntity> reviewList = spotRepository.findAllByLocationnum(locationNum);
        if(reviewList.isEmpty()){
            result.put("result", "fail");
            result.put("message", "등록된 리뷰가 없습니다.");
            return result;
        }else{
            result.put("result", "success");
            result.put("data", reviewList);
        }

        return result;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }

        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
