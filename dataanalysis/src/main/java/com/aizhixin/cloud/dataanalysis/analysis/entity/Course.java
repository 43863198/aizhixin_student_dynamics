package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-20
 */
@Entity
@Table(name = "T_COURSE")
@ToString
public class Course implements Serializable {

    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    private Long id;
    /*学校id*/
    @Column(name = "ORG_ID") @Getter @Setter private Long orgId;
    /*课程号*/
    @Column(name = "COURSE_NUMBER")
    @Getter @Setter private String courseNumber;
    /*课程名称*/
    @Column(name = "COURSE_NAME")
    @Getter @Setter private String courseName;
    /*学分*/
    @Column(name = "CREDIT")
    @Getter @Setter private float credit ;
    /*总学时*/
    @Column(name = "TOTAL_HOURS")
    @Getter @Setter private int totalHours ;
    /*课程开设单位号*/
    @Column(name = "SUBORDINATE_UNIT")
    @Getter @Setter private String subordinateUnit;
    /*课程类型*/
    @Column(name = "COURSE_TYPE")
    @Getter @Setter private String courseType;
    /*是否停课*/
    @Column(name = "STOP_CLASS")
    @Getter @Setter private int StopClass;
    @ApiModelProperty(value = "是否删除标志")
    @Column(name = "DELETE_FLAG")
    @Getter @Setter protected Integer deleteFlag = DataValidity.VALID.getState();

}
