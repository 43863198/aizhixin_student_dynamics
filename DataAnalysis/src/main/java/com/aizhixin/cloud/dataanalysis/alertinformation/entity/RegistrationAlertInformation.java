package com.aizhixin.cloud.dataanalysis.alertinformation.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
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
@Table(name = "T_REGISTRATION_ALERT_INFORMATION")
@ToString
public class RegistrationAlertInformation extends AbstractEntity {

    /*
    *  姓名
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
     * 所属学院
     */
    @NotNull
    @Column(name = "COLLOGE_NAME")
    @Getter @Setter private String collogeName;

    /*
    * 所属年级
    */
    @NotNull
    @Column(name = "GRADE")
    @Getter @Setter private String grade;

    /*
     * 所属班级
     */
    @NotNull
    @Column(name = "CLASS_NAME")
    @Getter @Setter private String className;

    /*
     * 预警类型
     */
    @NotNull
    @Column(name = "WARNING_TYPE")
    @Getter @Setter private String warningType;

    /*
     * 预警等级
     */
    @NotNull
    @Column(name = "WARNING_LEVEL")
    @Getter @Setter private String warningLevel;

    /*
      * 预警条件
      */
    @NotNull
    @Column(name = "WARNING_CONDITION")
    @Getter @Setter private String warningCondition;

    @ApiModelProperty(value = "预警时间")
    @CreatedDate
    @Column(name = "WARNING_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter protected Date warningTime = new Date();



}
