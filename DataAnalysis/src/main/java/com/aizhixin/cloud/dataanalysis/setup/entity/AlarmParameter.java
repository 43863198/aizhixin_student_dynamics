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
 * @Date: 2017-11-13
 */
@Entity
@Table(name = "T_ALARM_PARAMETER")
@ToString
public class AlarmParameter extends AbstractEntity {

    /*
   * 设置id
   */
    @NotNull
    @Column(name = "ALARMSETTINGS_ID")
    @Getter @Setter private AlarmSettings alarmSettings;

    /*
    * 等级
    */
    @NotNull
    @Column(name = "LEVEL")
    @Getter @Setter private int level;

    /*
     * 参数
     */
    @NotNull
    @Column(name = "SET_PARAMETER")
    @Getter @Setter private int setParameter;

    /*
     * 描述
     */
    @NotNull
    @Column(name = "DESCRIBE")
    @Getter @Setter private String describe;

}
