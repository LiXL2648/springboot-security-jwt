package com.zghky.waterCollection.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysRole implements Serializable {
    private Integer id;

    private String roleName;

    private String roleCode;

    private String roleDesc;

    private Boolean sort;

    private Boolean status;

    private Timestamp createTime;

    private Timestamp updateTime;
}