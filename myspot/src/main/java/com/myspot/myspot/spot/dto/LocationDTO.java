package com.myspot.myspot.spot.dto;

import com.myspot.myspot.spot.domain.entity.LocationEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LocationDTO {
    private Integer locationNum;
    private String locationName;
    private String locationCode;
    private String locationMapCode;
    private String locationLangitude;
    private String locationLongitude;
    private String locationAddress;
    public LocationEntity toEntity() {
        return LocationEntity.builder()
                .locationName(locationName)
                .location_map_code(locationCode)
                .location_map_code(locationMapCode)
                .locationLatitude(locationLangitude)
                .locationLongitude(locationLongitude)
                .locationAddress(locationAddress)
                .build();
    }

    public LocationDTO(String locationName, String locationCode, String locationLangitude, String locationLongitude, String locationMapCode, String locationAddress){
        this.locationNum = Integer.valueOf(locationMapCode);
        this.locationName = locationName;
        this.locationCode = locationCode;
        this.locationLangitude = locationLangitude;
        this.locationLongitude = locationLongitude;
        this.locationMapCode = locationMapCode;
        this.locationAddress = locationAddress;
    }
}
