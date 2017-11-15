package com.aizhixin.cloud.dataanalysis.alertinformation.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-13
 */
@Entity
@Table(name = "T_ALERT_WARNING_INFORMATION")
@ToString
public class AlertWarningInformation extends AbstractEntity {

    /*
   *  告警人id
   */
    @NotNull
    @Column(name = "DEFENDANT_ID")
    @Getter
    @Setter
    private Long defendantId;

    /*
    *  告警人姓名
    */
    @NotNull
    @Column(name = "NAME")
    @Getter
    @Setter
    private String name;

    /*
     * 学号/工号
     */
    @NotNull
    @Column(name = "JOB_NUMBER")
    @Getter @Setter private String jobNumber;

    /*
     * 学校id
     */
    @NotNull
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;
    /*
     * 所属学院id
     */
    @NotNull
    @Column(name = "COLLOGE_ID")
    @Getter @Setter private Long collogeId;

    /*
     * 所属学院
     */
    @NotNull
    @Column(name = "COLLOGE_NAME")
    @Getter @Setter private String collogeName;


    /*
   * 所属专业id
   */
    @NotNull
    @Column(name = "PROFESSIONAL_ID")
    @Getter @Setter private Long professionalId;

    /*
     * 所属专业
     */
    @NotNull
    @Column(name = "PROFESSIONAL_NAME")
    @Getter @Setter private String professionalName;

    /*
   * 所属班级id
   */
    @NotNull
    @Column(name = "CLASS_ID")
    @Getter @Setter private Long classId;

    /*
     * 所属班级
     */
    @NotNull
    @Column(name = "CLASS_NAME")
    @Getter @Setter private String className;

    /*
   * 学年
   */
    @NotNull
    @Column(name = "TEACHING_YEAR")
    @Getter @Setter private String teachingYear;

    /*
     * 预警类型
     */
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
   	@JoinColumn(name = "WARNING_TYPE")
    @Getter @Setter private WarningType warningType;

    /*
     * 预警等级
     */
    @NotNull
    @Column(name = "WARNING_LEVEL")
    @Getter @Setter private int warningLevel;

    /*
     * 告警时间
     */
    @ApiModelProperty(value = "告警时间")
    @CreatedDate
    @Column(name = "WARNING_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter protected Date warningTime = new Date();



}
