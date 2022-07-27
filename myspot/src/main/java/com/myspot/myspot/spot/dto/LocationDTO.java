package com.myspot.myspot.spot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@ToString
public class LocationDTO {
    private List<String> locationName;
    private List<String> locationCode;

    public LocationDTO(List<String> locationName, List<String> locationCode){
        this.locationName = locationName;
        this.locationCode = locationCode;
    }
}
