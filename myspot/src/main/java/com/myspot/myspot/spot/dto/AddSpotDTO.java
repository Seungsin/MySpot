package com.myspot.myspot.spot.dto;

import com.myspot.myspot.spot.domain.entity.LocationEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AddSpotDTO {
    private String user_email;
    private String locationMapCode;
    private String locationLongitude;
    private String locationLatitude;
    private String locationName;
    private String locationAddress;
    private String spotCategory;
    private String spotComment;
    private String spotFolder;
    private List<String> spotTag;
    private List<MultipartFile> spotImg;

}
