package com.myspot.myspot.spot.controller;

import com.myspot.myspot.spot.dto.AddSpotDTO;
import com.myspot.myspot.spot.dto.SpotDto;
import com.myspot.myspot.spot.service.SpotService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@AllArgsConstructor
public class SpotController {
    private SpotService spotService;

    //사용자의 스팟을 등록함
    @PostMapping("/spot/addspot")
    public ResponseEntity<Map<String, Object>> addNewSpot(AddSpotDTO addSpotDTO) throws IOException {
        Map<String, Object> result = spotService.addNewSpot(addSpotDTO);
        if (result.get("result").equals("success")){
            return ResponseEntity.ok().body(result);
        }else{
            return ResponseEntity.badRequest().body(result);
        }
    }

    //사용자의 스팟을 지움
    @GetMapping("/spot/deletespot")
    public ResponseEntity<Map<String, Object>> deleteSpot(@RequestParam String email, @RequestParam int spotNum){
        Map<String, Object> result = spotService.deleteSpot(email, spotNum);
        if (result.get("result").equals("success")){
            return ResponseEntity.ok().body(result);
        }else{
            return ResponseEntity.badRequest().body(result);
        }
    }

    //사용자의 모든 스팟 정보를 불러옴
    @GetMapping("/spot/userSpot")
    public ResponseEntity<Map<String, Object>> findUserSpot(@RequestParam("email") String email){
        return ResponseEntity.ok().body(spotService.findUserSpot(email));
    }

    //목록에서 스팟 정보를 불러옴
    @GetMapping("/spot/getSpotList")
    public ResponseEntity<Map<String, Object>> getSpotList(@RequestParam String searchWord, @RequestParam String category, @RequestParam String keyword, @RequestParam Double lat, @RequestParam Double lon){
        Map<String, Object> responseBody = spotService.getSpotList(searchWord, category, keyword, lat, lon);

        if(responseBody.get("result").equals("success")){
            return ResponseEntity.ok().body(responseBody);
        }else{
            return ResponseEntity.internalServerError().body(responseBody);
        }
    }

    //개별적인 스팟 리뷰들을 불러옴
    @GetMapping("/spot/getSpotReviews")
    public ResponseEntity<Map<String, Object>> getSpotReviews(@RequestParam("locationCode") String locationCode){
        Map<String, Object> responseBody = spotService.getSpotReviews(locationCode);

        if(responseBody.get("result").equals("success")){
            return ResponseEntity.ok().body(responseBody);
        }else{
            return ResponseEntity.internalServerError().body(responseBody);
        }
    }
}
