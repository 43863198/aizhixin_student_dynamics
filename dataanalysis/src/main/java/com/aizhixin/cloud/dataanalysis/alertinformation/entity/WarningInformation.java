package com.aizhixin.cloud.dataanalysis.alertinformation.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractStringIdEntity;
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
@Table(name = "T_WARNING_INFORMATION")
@ToString
public class WarningInformation extends AbstractStringIdEntity {

    /*
    *  告警人姓名
    */
    @Column(name = "NAME")
    @Getter
    @Setter
    private String name;

    /*
     * 学号/工号
     */
    @Column(name = "JOB_NUMBER")
    @Getter @Setter private String jobNumber;

    /*
     * 学校id
     */
    @NotNull
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;
    /*
     * 所属学院code
     */
    @Column(name = "COLLOGE_CODE")
    @Getter @Setter private String collogeCode;

    /*
     * 所属学院
     */
    @Column(name = "COLLOGE_NAME")
    @Getter @Setter private String collogeName;


    /*
   * 所属专业code
   */
    @Column(name = "PROFESSIONAL_CODE")
    @Getter @Setter private String professionalCode;

    /*
     * 所属专业
     */
    @Column(name = "PROFESSIONAL_NAME")
    @Getter @Setter private String professionalName;

    /*
   * 所属班级code
   */
    @Column(name = "CLASS_CODE")
    @Getter @Setter private String classCode;

    /*
     * 所属班级
     */
    @Column(name = "CLASS_NAME")
    @Getter @Setter private String className;


    /*
     * 手机号
     */
    @Column(name = "PHONE")
    @Getter @Setter private String phone;

    /*
     * 家庭住址
     */
    @Column(name = "ADDRESS")
    @Getter @Setter private String address;

    /*
     * 家长联系方式
     */
    @Column(name = "PARENTS_CONTACT")
    @Getter @Setter private String parentsContact;

    /*
     * 预警类型
     */
    @NotNull
//    @Pattern(regexp = "[a-zA-Z]+$")
    @Column(name = "WARNING_TYPE")
    @Getter @Setter private String warningType;

    /*
     * 预警等级
     */
    @Column(name = "WARNING_LEVEL")
    @Getter @Setter private int warningLevel;

    /*
     *  预警对象类型(60老师，70学生)
     */
    @Column(name = "USER_TYPE")
    @Getter @Setter private int userType;

    /*
    * 状态(10:处理中；20：已处理)
    */
    @Column(name = "WARNING_STATE")
    @Getter @Setter private int warningState;

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

    /*
     * 预警引擎设置id
     */
    @Column(name = "ALARMSETTINGS_ID")
    @Getter @Setter private String alarmSettingsId;
    /*
     * 预警标准
     */
     @Column(name = "WARNING_STANDARD")
     @Getter @Setter private String warningStandard;

    /*
     * 产生预警条件
     */
    @Column(name = "WARNING_CONDITION")
    @Getter @Setter private String warningCondition;
    /*
     * 是否归档
     */
    @Column(name = "IS_FILE")
    @Getter @Setter private int isFile;

    /*
     * 预警产生源数据
     */
    @Column(name = "WARNING_SOURCE")
    @Getter @Setter private String warningSource;

    /*
    * 学期
    */
    @Column(name = "SEMESTER")
    @Getter @Setter private String semester;
    /*
     * 学年
     */
    @Column(name = "TEACHING_YEAR")
    @Getter @Setter private String teacherYear;
}
