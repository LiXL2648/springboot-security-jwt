package com.zghky.waterCollection.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysUser implements Serializable {
    private Integer id;

    private String username;

    private String password;

    private String phone;

    private String email;

    private Boolean enabled;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String roles;
}