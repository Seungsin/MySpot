package com.myspot.myspot.user.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MySpotReviewDto {
    private List<String> spot_Photo;
    private String comment;
    private Date reviewDate;

    @Builder
    public MySpotReviewDto(List photo, String comment, Date reviewDate){
        this.spot_Photo = photo;
        this.reviewDate = reviewDate;
        this.comment = comment;
    }
}
