package com.aizhixin.cloud.dataanalysis.setup.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-13
 */
@Entity
@Table(name = "T_ALARM_RULE")
@ToString
public class AlarmRule extends AbstractEntity {

    /*
    * 规则名称
    */
    @Column(name = "NAME")
    @Getter @Setter private String name;

    /*
     * 开启状态(10:启用 ;20:关闭；)
     */
    @NotNull
    @Column(name = "SETUP_CLOSE_FLAG")
    @Getter @Setter private int setupCloseFlag;

    /*
    * 机构id
    */
    @NotNull
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;

    /*
     * 规则左区间参数值
     */
    @Column(name = "LEFT_PARAMETER")
    @Getter @Setter private int leftParameter;

    /*
     * 规则左区间参数关系(大于,大于等于)
     */
    @Column(name = "LFET_RELATIONSHIP")
    @Getter @Setter private String leftRelationship;

    /*
    * 规则右区间参数值
    */
    @Column(name = "RIGHT_PARAMETER")
    @Getter @Setter private int rightParameter;

    /*
     * 规则右区间参数关系(大于,大于等于)
     */
    @Column(name = "RIGHT_RELATIONSHIP")
    @Getter @Setter private String rightRelationship;

}