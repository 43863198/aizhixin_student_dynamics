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
@Table(name = "T_CURRICULUM_SCHEDULE")
@ToString
public class CurriculumSchedule implements Serializable {

    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    private Long id;
    /*
     * 学校id
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
     * 开始周
     */
    @Column(name = "START_WEEK")
    @Getter
    @Setter
    private int startWeek;
    /*
     * 结束周
     */
    @Column(name = "END_WEEK")
    @Getter
    @Setter
    private int endWeek;
    /*
     * 星期
     */
    @Column(name = "DAY_OF_THE_WEEK")
    @Getter
    @Setter
    private int dayOfTheWeek;
    /*
     * 开始节
     */
    @Column(name = "START_PERIOD")
    @Getter
    @Setter
    private int startPeriod;
    /*
     * 持续节数
     */
    @Column(name = "PERIOD_NUM")
    @Getter
    @Setter
    private int periodNumber;

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
