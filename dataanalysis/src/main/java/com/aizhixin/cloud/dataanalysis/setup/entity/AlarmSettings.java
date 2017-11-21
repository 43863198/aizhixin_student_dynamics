package com.aizhixin.cloud.dataanalysis.setup.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
     * 机构id
     */
    @NotNull
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;

    /*
   * 预警类型
   */
    @NotNull
    @Column(name = "TYPE")
    @Getter
    @Setter
    private String warningType;

    /*
     * 产生预警条件
     */
    @NotNull
    @Column(name = "WARNING_CONDITION")
    @Getter @Setter private String warningCondition;


    /*
     * 预警标准
     */
    @NotNull
    @Column(name = "WARNING_STANDARD")
    @Getter @Setter private String warningStandard;


    /*
   * 开启状态(10:启用 ;20:关闭；)
   */
    @NotNull
    @Column(name = "SETUP_CLOSE")
    @Getter @Setter private int setupCloseFlag;


}
