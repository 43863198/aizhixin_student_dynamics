package com.aizhixin.cloud.dataanalysis.setup.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
@Entity
@Table(name = "T_WARNING_TYPE")
@ToString
public class WarningType extends AbstractEntity {
    /*
     * 机构id
     */
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;

    /*
     * 预警类型
     */
    @Column(name = "TYPE")
    @Getter @Setter private String type;

    /*
    * 预警类型描述
    */
    @Column(name = "TYPE_DESCRIBE")
    @Getter @Setter private String typeDescribe;

    /*
   * 预警名称
   */
    @Column(name = "WARNING_NAME")
    @Getter @Setter private String warningName;

    /*
    * 预警描述
    */
    @Column(name = "WARNING_DESCRIBE")
    @Getter @Setter private String warningDescribe;

    /*
     * 开启或关闭(10开启；20关闭)
     */
    @Column(name = "SETUP_CLOSE_FLAG")
    @Getter @Setter private int setupCloseFlag;

    /*
    * 告警开始时间
    */
    @ApiModelProperty(value = "预警起始时间")
    @CreatedDate
    @Column(name = "START_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter protected Date startTime;

    /*
     * 告警结束时间
     */
    @ApiModelProperty(value = "预警结束时间")
    @CreatedDate
    @Column(name = "END_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter protected Date endTime;

}
