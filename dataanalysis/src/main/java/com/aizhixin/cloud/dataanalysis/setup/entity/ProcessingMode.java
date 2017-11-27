package com.aizhixin.cloud.dataanalysis.setup.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-23
 */
@Entity
@Table(name = "T_PROCESSING_MODE")
@ToString
public class ProcessingMode extends AbstractEntity {
    /*
     * 预警类型
      */
    @NotNull
    @Column(name = "WARNING_TYPE")
    @Getter @Setter private String warningType;

    /*
     * 预警级别
     */
    @Column(name = "WARNING_LEVEL")
    @Getter @Setter private int warningLevel;

    /*
    * 预警处理操作类型集合(发送学生10 发送辅导员 20 发送院系领导)
    */
    @Column(name = "OPERATION_TYPE_SET")
    @Getter @Setter private String operationTypeSet;


    /*
     * 开启状态(10:启用 ;20:关闭；)
     */
    @NotNull
    @Column(name = "SETUP_CLOSE_FLAG")
    @Getter @Setter private int setupCloseFlag;

    /*
     * 预警处理操作集合(手机短信 1 电子邮件 2 站内信 3)
     */
    @Column(name = "OPERATION_SET")
    @Getter @Setter private String operationSet;

    /*
     * 机构id
     */
    @NotNull
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;

}
