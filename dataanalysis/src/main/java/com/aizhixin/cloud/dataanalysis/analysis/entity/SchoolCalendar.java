package com.aizhixin.cloud.dataanalysis.analysis.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-18
 */
@Entity
@Table(name = "T_SCHOOL_CALENDAR")
@ToString
public class SchoolCalendar implements Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    private Long id;
    /* 学校id*/
    @Column(name = "ORG_ID") @Getter @Setter private Long orgId;
    /* 学年*/
    @Column(name = "TEACHER_YEAR")
    @Getter @Setter private Integer teacherYear;
    /* 学期*/
    @Column(name = "SEMESTER")
    @Getter @Setter private String semester;
    /* 开始时间*/
    @Column(name = "START_TIME")
    @Getter @Setter private Date StartTime;
    /* 周时*/
    @Column(name = "WEEK")
    @Getter @Setter int week;

}
