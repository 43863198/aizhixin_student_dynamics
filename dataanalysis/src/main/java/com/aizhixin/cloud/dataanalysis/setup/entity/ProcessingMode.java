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
     * 预警设置id
     */
    @NotNull
    @Column(name = "ALARMSETTINGS_ID")
    @Getter @Setter private String alarmSettingsId;

    /*
    * 预警处理操作类型集合(短信通知，辅导员和学生面谈，院系教务和家长电话联系等)
    */
    @Column(name = "OPERATION_TYPE_SET")
    @Getter @Setter private String operationTypeSet;

    /*
     * 机构id
     */
    @NotNull
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;

}
