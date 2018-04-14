package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-14
 */
@Entity
@Table(name = "T_TEACHINGCLASS_STUDENTS")
@ToString
public class TeachingClassStudents implements Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    private Long id;
    /*
    * 组织id
    */
    @Column(name = "ORG_ID")
    @Getter
    @Setter
    private Long orgId;
    /*
    * 教学班名称
    */
    @Column(name = "TEACHING_CLASS_NAME")
    @Getter
    @Setter
    private String teachingClassName;
    /*
     * 课程名称
     */
    @Column(name = "COURSE_NAME")
    @Getter
    @Setter
    private String courseName;
    /*
     *学生学号
     */
    @Column(name = "STRUDENT_JOB_NUMBER")
    @Getter
    @Setter
    private String studnetJobNumber;

}
