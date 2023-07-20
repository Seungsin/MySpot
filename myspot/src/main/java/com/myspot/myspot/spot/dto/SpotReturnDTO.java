package com.myspot.myspot.spot.dto;

import com.myspot.myspot.spot.domain.entity.SpotEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SpotReturnDTO {
    private String useremail;
    private int location_num;
    private String locationName;
    private String spotName;
    private String spotcategory;
    private String spotComment;

    private List<String> spotPhoto;
    private String spotFolder;
    private Date spotDate;


    public SpotReturnDTO(SpotEntity spotEntity){
        this.useremail = spotEntity.getUseremail();
        this.spotName = spotEntity.getSpot_name();
        this.location_num = spotEntity.getSpot_number();
        this.spotcategory = spotEntity.getSpot_category();
        this.spotComment = spotEntity.getSpot_Comment();
        this.spotFolder = spotEntity.getSpot_Folder();
        this.spotDate = spotEntity.getSpot_date();

    }

    public void setSpotPhotoList(List<String> spotPhoto){
        this.spotPhoto = spotPhoto;
    }
}
