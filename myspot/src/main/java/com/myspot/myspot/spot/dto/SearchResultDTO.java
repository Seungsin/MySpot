package com.myspot.myspot.spot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SearchResultDTO {
    private String locationCode;
    private String locationName;
    private String locationAddress;
    private String locationLongitude;
    private String locationLatitude;
    private String spotCount;

    public SearchResultDTO(String locationCode, String locationAddress, String locationLongitude, String locationLatitude, String locationName, int spotCount){
        this.locationCode = locationCode;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.locationAddress = locationAddress;
        this.locationName = locationName;
        this.spotCount = Integer.toString(spotCount);
    }
}
