package com.myspot.myspot.spot.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "spot_location")
public class LocationEntity {
    @Id
    private Integer location_num;

    @Column(name="location_Map_Code", length = 100, nullable = false)
    private String locationmapcode;

    @Column(length = 100, nullable = false)
    private String locationname;

    @Column(length = 100)
    private String locationLongitude;

    @Column(length = 100)
    private String locationLatitude;

    @Column(length = 200)
    private String locationAddress;

    //private LocalDateTime  userlastdate;

    @Builder
    public LocationEntity(String location_map_code, String locationName, String locationLatitude, String locationLongitude, String locationAddress) {
        this.location_num = Integer.valueOf(location_map_code);
        this.locationname = locationName;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.locationmapcode = location_map_code;
        this.locationAddress = locationAddress;
    }
}
