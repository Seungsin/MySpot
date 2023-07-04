package com.myspot.myspot.spot.controller;

import com.myspot.myspot.spot.dto.LocationDTO;
import com.myspot.myspot.spot.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class LocationController {
    private LocationService locationService;

    @GetMapping("/location/citylist")
    public Map<String, Map<String, List<String>>> getCityList(){
        return locationService.getCity();
    }

    @PostMapping("/location/addLocationInfo")
    public ResponseEntity<Map<String, Object>> addLocationInfo(@RequestBody LocationDTO locationDTO){
        return ResponseEntity.ok().body(locationService.addLocationInfo(locationDTO));
    }

}

