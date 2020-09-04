package com.zghky.waterCollection.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WaterCollection implements Serializable {
    private Integer id;

    private String province;

    private String city;

    private String county;

    private String street;

    private String locationCode;

    private String villageName;

    private String villageHead;

    private String villageHeadPhone;

    private Integer villagePopulation;

    private Double villageArea;

    private String waterNum;

    private String waterName;

    private String waterType;

    private String involvedVillages;

    private String waterManager;

    private String unit;

    private String position;

    private String waterManagerPhone;

    private Integer waterArea;

    private Integer watersLength;

    private Integer watersWidth;

    private String startPoint;

    private String startLongitude;

    private String startDimension;

    private String endPoint;

    private String endLongitude;

    private String endDimension;

    private String photosPath;

    private Double transparency;

    private Double dissolvedOxygen;

    private Double ammoniaNitrogen;

    private String mainProblem;

    private Integer hasGoverned;

    private String governProgress;

    private Timestamp createTime;

    private Integer createUserId;

    private Timestamp updateTime;

    private Integer updateUserId;

    private String mapEstimate;

    private Boolean isDelete;

    private Boolean isUpdate;

    private String username;
}