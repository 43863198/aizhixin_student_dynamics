package com.aizhixin.cloud.dataanalysis.analysis.entity;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-14
 */
@Entity
@Table(name = "T_Examination_Arrangement")
@ToString
public class ExaminationArrangement implements Serializable {

    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter @Setter private Long id;
    /*
     * 学校id
     */
    @Column(name = "ORG_ID")
    @Getter
    @Setter
    private Long orgId;
    /*
     * 教课人姓名
     */
    @Column(name = "TEACHER_NAME")
    @Getter
    @Setter
    private String teacherName;
    /*
     * 教课人工号
     */
    @Column(name = "JOB_NUMBER")
    @Getter
    @Setter
    private String jboNumber;
    /*
     *教室编号
     */
    @Column(name = "CLASSROOM_NUMBER")
    @Getter
    @Setter
    private String classRommNumber;
    /*
     *考试日期
     */
    @Column(name = "TEST_DATE")
    @Getter
    @Setter
    private Date testDate;
    /*
     *考试开始时间
     */
    @Column(name = "START_TIME")
    @Getter
    @Setter
    private Time startTime;
    /*
     *考试结束时间
     */
    @Column(name = "END_TIME")
    @Getter
    @Setter
    private Time endTime;
    /*
     *考试时长
     */
    @Column(name = "WHEN_LONG")
    @Getter
    @Setter
    private String whenLong;
    /*
     *课程编号
     */
    @Column(name = "COURSE_NUMBER")
    @Getter
    @Setter
    private String courseNumber;

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
