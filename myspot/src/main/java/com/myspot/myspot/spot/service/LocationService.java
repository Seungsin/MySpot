package com.myspot.myspot.spot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myspot.myspot.spot.domain.repository.LocationRepository;
import com.myspot.myspot.spot.dto.LocationDTO;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class LocationService {
    private ObjectMapper mapper = new ObjectMapper();
    private LocationRepository locationRepository;

    @SneakyThrows
    public JsonNode getResponse(){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=*00";
        ResponseEntity<String> response
                =restTemplate.getForEntity(fooResourceUrl, String.class);

        JsonNode root = mapper.readTree(response.getBody());
        JsonNode name = root.path("regcodes");

        return name;
    }

    public Map<String, Map<String, List<String>>> getCity(){
        JsonNode res = getResponse();

        List<String> cityList = res.findValuesAsText("name");

        Map<String, Map<String, List<String>>> cityAllList = new HashMap<>();

        for(int i=0;i<cityList.size();i++){
            if(cityList.get(i).split(" ").length==1){
                cityAllList.put(cityList.get(i), new HashMap<>());
            }else if(cityList.get(i).split(" ").length==2){
                cityAllList.get(cityList.get(i).split(" ")[0])
                        .put(cityList.get(i).split(" ")[1], new ArrayList<String>());
            }else{
                cityAllList.get(cityList.get(i).split(" ")[0])
                        .get(cityList.get(i).split(" ")[1])
                        .add(cityList.get(i).split(" ")[2]);
            }
        }

        return cityAllList;
    }

    public Map<String, Object> addLocationInfo(LocationDTO locationDTO){
        Map<String, Object> result = new HashMap<>();

        System.out.println(locationDTO);

        if(locationDTO.getLocationMapCode() == null){
            result.put("result", "fail");
            result.put("message", "location map code가 없음");
            return result;
        }

        if(!locationRepository.findAllByLocationmapcode(locationDTO.getLocationMapCode()).isEmpty()){
            result.put("result", "fail");
            result.put("message", "해당 location은 이미 등록되어 있음");
            return result;
        }

        if(locationRepository.save(locationDTO.toEntity())==null){
            result.put("result", "fail");
        }else{
            result.put("result", "success");
            result.put("locationCode", locationRepository.findByLocationmapcode(locationDTO.getLocationMapCode()));
        }

        return result;
    }
}
