package com.myspot.myspot.spot.dto;

import com.myspot.myspot.spot.domain.entity.SpotTagEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SpotTagDTO {
    private String tagName;
    private Integer locationNum;
    private Integer spotNum;

    public SpotTagEntity toEntity() {
        return SpotTagEntity.builder()
                .tagName(tagName)
                .locationNum(locationNum)
                .spotNum(spotNum)
                .build();
    }

    public SpotTagDTO(int locationNum, int spotNum, String tagName) {
        this.tagName = tagName;
        this.locationNum = locationNum;
        this.spotNum = spotNum;
    }
}
