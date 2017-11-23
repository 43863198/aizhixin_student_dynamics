package com.aizhixin.cloud.dataanalysis.alertinformation.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-13
 */
@Entity
@Table(name = "T_WARNING_INFORMATION")
@ToString
public class WarningInformation extends AbstractEntity {

    /*
   *  告警人id
   */
    @Column(name = "DEFENDANT_ID")
    @Getter
    @Setter
    private Long defendantId;

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
     * 所属学院id
     */
    @Column(name = "COLLOGE_ID")
    @Getter @Setter private Long collogeId;

    /*
     * 所属学院
     */
    @Column(name = "COLLOGE_NAME")
    @Getter @Setter private String collogeName;


    /*
   * 所属专业id
   */
    @Column(name = "PROFESSIONAL_ID")
    @Getter @Setter private Long professionalId;

    /*
     * 所属专业
     */
    @Column(name = "PROFESSIONAL_NAME")
    @Getter @Setter private String professionalName;

    /*
   * 所属班级id
   */
    @Column(name = "CLASS_ID")
    @Getter @Setter private Long classId;

    /*
     * 所属班级
     */
    @Column(name = "CLASS_NAME")
    @Getter @Setter private String className;

    /*
     * 学年
     */
    @Column(name = "TEACHING_YEAR")
    @Getter @Setter private String teachingYear;

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
    @Pattern(regexp = "/[a-zA-Z\\u4E00-\\u9FA5]+$/")
    @Column(name = "WARNING_TYPE")
    @Getter @Setter private String warningType;

    /*
     * 预警等级
     */
    @Column(name = "WARNING_LEVEL")
    @Getter @Setter private int warningLevel;

    /*
    * 状态(10:告警中；20：已处理；30：处理中；40:取消)
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
     * 处理建议
     */
    @Column(name = "PROPOSAL")
    @Getter @Setter private String proposal;


}
