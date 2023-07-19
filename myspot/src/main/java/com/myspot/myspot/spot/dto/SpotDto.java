package com.myspot.myspot.spot.dto;

import com.myspot.myspot.spot.domain.entity.SpotEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class SpotDto {
    private String useremail;
    private int location_num;
    private String locationName;
    private String spotName;
    private String spotcategory;
    private String spotComment;

    private String spotPhoto;
    private String spotFolder;
    private Date spotDate;

    public SpotEntity toEntity() {
        return SpotEntity.builder()
                .useremail(useremail)
                .locationnum(location_num)

                .spotname(spotName)
                .spotcategory(spotcategory)
                .spotComment(spotComment)

                .spotPhoto(spotPhoto)
                .spotFolder(spotFolder)
                .spotDate(spotDate)
                .build();
    }

    public SpotDto(String useremail, int locationNum, String spotName, String spotcategory, String spotComment, String spotPhoto, String spotFolder, Date spotDate) {
        this.useremail = useremail;
        this.spotName = spotName;
        this.location_num = locationNum;
        this.spotcategory = spotcategory;
        this.spotComment = spotComment;

        this.spotPhoto = spotPhoto;
        this.spotFolder = spotFolder;
        this.spotDate = spotDate;
    }
}
