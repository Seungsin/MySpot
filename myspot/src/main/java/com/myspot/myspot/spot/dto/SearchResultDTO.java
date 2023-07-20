package com.myspot.myspot.spot.dto;

import com.myspot.myspot.spot.domain.entity.SpotEntity;
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
    private String locationPhoto;
    private String spotCount;

    public SearchResultDTO(String locationCode, String locationAddress, String locationLongitude, String locationLatitude, String locationName, int spotCount, String locationPhoto){
        this.locationCode = locationCode;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.locationAddress = locationAddress;
        this.locationName = locationName;
        this.locationPhoto = locationPhoto;
        this.spotCount = Integer.toString(spotCount);
    }
}
