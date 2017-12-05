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
import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@Entity
@Table(name = "T_OPERATION_RECORD")
@ToString
public class OperationRecord extends AbstractEntity {

    /*
     *  预警信息id
    */
    @Column(name = "WARNINGINFORMATION_ID")
    @Getter @Setter private String warningInformationId;

    /*
   * 状态(10:告警中；20：已处理；40:取消)
   */
    @Column(name = "WARNING_STATE")
    @Getter @Setter private int warningState;


    /*
    * 预警处理方式设置id
    */
    @Column(name = "PROCESSINGMODE_ID")
    @Getter @Setter private String processingModeId;


    /*
   * 预警处理操作类型(短信通知，辅导员和学生面谈，院系教务和家长电话联系等)
   */
    @Column(name = "OPERATION_TYPE")
    @Getter @Setter private String operationType;


    /*
     * 预警处理建议
     */
    @Column(name = "PROPOSAL")
    @Getter @Setter private String proposal;

    /*
    * 处理时间
    */
    @CreatedDate
    @Column(name = "OPERATION_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter protected Date operationTime = new Date();

    /*
     * 所属学期id
     */
    @Column(name = "SEMESTER_ID")
    @Getter @Setter private Long semesterId;

    /*
     *处理人员id
     */
    @Column(name = "OPERATOR_ID")
    @Getter @Setter private Long OperatorId;

    /*
    * 机构id
    */
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;

    /*
     *处理类型 辅导员处理10 学院处理 20
     */
    @Column(name = "DEALT_YPE")
    @Getter @Setter private int dealType;

}
