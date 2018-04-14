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
@Table(name = "T_TEACHING_CLASS")
@ToString
public class TeachingClass implements Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter @Setter private Long id;
    /*
     * 组织id
     */
    @Column(name = "ORG_ID")
    @Getter
    @Setter
    private Long orgId;
    /*
     * 教学班编号
     */
    @Column(name = "TEACHING_CLASS_NUMBER")
    @Getter
    @Setter
    private String teachingClassNumber;
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
     * 上课地点
     */
    @Column(name = "CLASS_PLACE")
    @Getter
    @Setter
    private String classPlace;
    /*
     * 学期
     */
    @Column(name = "SEMESTER")
    @Getter
    @Setter
    private String semester;
    /*
     * 教师姓名
     */
    @Column(name = "TEACHER_NAME")
    @Getter
    @Setter
    private String teacherName;
    /*
     * 教师工号
     */
    @Column(name = "JOB_NUMBER")
    @Getter
    @Setter
    private String jubNumber;
    /*
     * 上课班级
     */
    @Column(name = "CLASSES")
    @Getter
    @Setter
    private String classes;

    @ApiModelProperty(value = "创建时间")
    @CreatedDate
    @Column(name = "CREATED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter protected Date createdDate = new Date();

    @ApiModelProperty(value = "最后一次修改人")
    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    @Getter @Setter protected Long lastModifiedBy;

    @ApiModelProperty(value = "最后一次修改时间")
    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter protected Date lastModifiedDate = new Date();

    @ApiModelProperty(value = "是否删除标志")
    @Column(name = "DELETE_FLAG")
    @Getter @Setter protected Integer deleteFlag = DataValidity.VALID.getState();

}
