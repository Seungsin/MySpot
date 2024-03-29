package com.myspot.myspot.spot.setting;

// UploadReqDto.java

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UploadReqDto {
    private String bucketName;
    private String uploadFileName;
    private String localFileLocation;
}
