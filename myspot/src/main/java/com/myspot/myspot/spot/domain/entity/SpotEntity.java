package com.myspot.myspot.spot.domain.entity;


import com.google.api.client.util.DateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user_spot")
public class SpotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int spot_number;

    @Column(length = 100, nullable = false)
    private String useremail;

    @Column(name = "location_Num")
    private Integer locationnum;

    @Column(length = 100, nullable = false)
    private String spot_name;

    private String spot_category;

    private String spot_Comment;

    @Column(length = 1000)
    private String spot_Photo;

    @Column(length = 100)
    private String spot_Folder;

    private Date spot_date;

    @Builder
    public SpotEntity(String useremail, int locationnum, String spotname, String spotcategory, String spotComment, String spotPhoto, String spotFolder, Date spotDate) {
        this.useremail = useremail;
        this.spot_name = spotname;
        this.locationnum = locationnum;
        this.spot_category = spotcategory;
        this.spot_Comment = spotComment;
        this.spot_Photo = spotPhoto;
        this.spot_Folder = spotFolder;
        this.spot_date = spotDate;
    }


}
