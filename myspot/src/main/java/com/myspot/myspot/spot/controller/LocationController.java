package com.myspot.myspot.spot.controller;

import com.myspot.myspot.spot.dto.LocationDTO;
import com.myspot.myspot.spot.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class LocationController {
    private LocationService locationService;

    @GetMapping("/location/city")
    public LocationDTO getCityList(){
        return locationService.getCity();
    }

    @GetMapping("/location/gu")
    public LocationDTO getGuList(@RequestParam String code){
        return locationService.getGu(code);
    }

    @GetMapping("/location/dong")
    public LocationDTO getDongList(@RequestParam String code){
        return locationService.getDong(code);
    }
}

