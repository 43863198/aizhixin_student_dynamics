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
 * @Date: 2017-11-24
 */
@Entity
@Table(name = "T_WARNING_TYPE")
@ToString
public class WarningType extends AbstractEntity {

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
    @Getter @Setter private String warningType;

    /*
   * 预警名称
   */
    @NotNull
    @Column(name = "WARNING_NAME")
    @Getter @Setter private String warningName;

    /*
    * 预警描述(多个时候使用“,”分割;描述和序号用“-”分隔开)
    */
    @NotNull
    @Column(name = "WARNING_DESCRIBE")
    @Getter @Setter private String warningDescribe;

    /*
     * 开启或关闭(10开启；20关闭)
     */
    @NotNull
    @Column(name = "SETUP_CLOSE_FLAG")
    @Getter @Setter private int setupCloseFlag;

}
