package com.li.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu implements Serializable {
    private Integer id;

    private Integer menuPid;

    private String menuPids;

    private Boolean isLeaf;

    private String menuName;

    private String url;

    private String icon;

    private String iconColor;

    private Integer sort;

    private Integer level;

    private Boolean status;

    private List<SysMenu> children;

    private Timestamp createTime;

    private Timestamp updateTime;
}