package com.myspot.myspot.spot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myspot.myspot.spot.dto.LocationDTO;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    private ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public JsonNode getResponse(String regcode){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=" + regcode;
        ResponseEntity<String> response
                =restTemplate.getForEntity(fooResourceUrl, String.class);

        JsonNode root = mapper.readTree(response.getBody());
        JsonNode name = root.path("regcodes");

        return name;
    }

    public LocationDTO getCity(){
        JsonNode res = getResponse("*00000000");

        List<String> cityList = res.findValuesAsText("name");
        List<String> cityCode = res.findValuesAsText("code");

        for(int i=0;i<cityCode.size();i++){
            cityCode.set(i, cityCode.get(i).substring(0,2));
        }
        cityList.add(0, "선택없음");
        cityCode.add(0, "00");

        LocationDTO locationDTO = new LocationDTO(cityList, cityCode);

        return locationDTO;
    }

    public LocationDTO getGu(String Code){
        //code가 2자리가 아닐때 예외처리

        JsonNode res = getResponse(Code + "*000000");

        List<String> cityList = res.findValuesAsText("name");
        List<String> cityCode = res.findValuesAsText("code");

        cityList.set(0, "선택없음");
        for(int i=1;i<cityList.size();i++){
            cityList.set(i, cityList.get(i).split(" ")[1]);
        }
        for(int i=0;i<cityCode.size();i++){
            cityCode.set(i, cityCode.get(i).substring(0,4));
        }
        LocationDTO locationDTO = new LocationDTO(cityList, cityCode);

        return locationDTO;
    }

    public LocationDTO getDong(String Code){
        //code가 4자리가 아닐때 예외처리
        JsonNode res = getResponse(Code + "*");

        List<String> cityList = res.findValuesAsText("name");
        List<String> cityCode = res.findValuesAsText("code");

        cityList.set(0, "선택없음");
        for(int i=1;i<cityList.size();i++){
            cityList.set(i, cityList.get(i).split(" ")[2]);
        }

        LocationDTO locationDTO = new LocationDTO(cityList, cityCode);

        return locationDTO;
    }
}
