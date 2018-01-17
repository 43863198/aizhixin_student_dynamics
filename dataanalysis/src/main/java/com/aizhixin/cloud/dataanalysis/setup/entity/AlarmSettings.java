package com.aizhixin.cloud.dataanalysis.setup.entity;

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
 * @Date: 2017-11-13
 */
@Entity
@Table(name = "T_ALARM_SETTINGS")
@ToString
public class AlarmSettings extends AbstractEntity {

    /*
   * 预警类型
   */
    @Column(name = "WARNING_TYPE")
    @Getter @Setter private String warningType;

    /*
      * 预警等级
      */
    @Column(name = "WARNING_LEVEL")
    @Getter @Setter private int warningLevel;

    /*
    * 应用规则id集合
    */
    @Column(name = "RULE_SET")
    @Getter @Setter private String ruleSet;

    /*
     * 规则集合关系
     */
    @Column(name = "RELATIONSHIP")
    @Getter @Setter private String relationship;

    /*
    * 机构id
    */
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;

    /*
     * 告警开始时间
     */
    @ApiModelProperty(value = "规则预警起始时间")
    @CreatedDate
    @Column(name = "START_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter protected Date startTime;


    /*
     * 告警结束时间
     */
    @ApiModelProperty(value = "规则预警结束时间")
    @CreatedDate
    @Column(name = "END_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter protected Date endTime;

    /*
    * 开启状态(10:启用 ;20:关闭；)
    */
    @NotNull
    @Column(name = "SETUP_CLOSE_FLAG")
    @Getter @Setter private int setupCloseFlag;

}
