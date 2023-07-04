package com.myspot.myspot.spot.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "spot_tag")

public class SpotTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tagNum;

    @Column(length = 50, nullable = false)
    private String tagName;

    private Integer locationNum;
    private Integer spotNum;

    @Builder
    public SpotTagEntity(int locationNum, int spotNum, String tagName) {
        this.tagName = tagName;
        this.locationNum = locationNum;
        this.spotNum = spotNum;
    }
}
