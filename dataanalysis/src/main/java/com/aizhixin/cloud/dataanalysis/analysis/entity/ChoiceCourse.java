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
 * @Date: 2018-04-20
 */
@Entity
@Table(name = "T_CHOICE_COURSE")
@ToString
public class ChoiceCourse implements Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter @Setter
    private Long id;
    /*学校id*/
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;
    /*课程号*/
    @Column(name = "COURSE_NUMBER")
    @Getter @Setter private String courseNumber;
    /*课序号*/
    @Column(name = "SERIAL_NUMBER")
    @Getter @Setter private String serialNumber;
    /*选课日期*/
    @Column(name = "CHOICE_DATE")
    @Getter @Setter private String choiceDate;
    /*选课时间*/
    @Column(name = "CHOICE_TIME")
    @Getter @Setter private String choiceTime;
    /*教学班号*/
    @Column(name = "TEACHING_CLASS_NUMBER")
    @Getter @Setter private String teachingClassNumber;
    /*学号*/
    @Column(name = "JOB_NUMBER")
    @Getter @Setter private String jobNmuber;

}
