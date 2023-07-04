package com.myspot.myspot.user.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MySpotListDto {
    private int key;
    private String spotName;
    private String address;
    private String latitude;
    private String longitude;
    private int spotCount;

    @Builder
    public MySpotListDto(int key, String spotName, String address, String latitude, String longitude, int spotCount) {
        this.key=key;
        this.spotCount = spotCount;
        this.spotName = spotName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
