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
    private WarningType warningType;

    /*
	 * 预警名称
	 */
    @NotNull
    @Column(name = "NAME")
    @Getter
    @Setter
    private String name;

    /*
     * 开启状态(10:启用 ;20:关闭；)
     */
    @NotNull
    @Column(name = "SETUP_CLOSE")
    @Getter @Setter private int setupCloseFlag;


}
