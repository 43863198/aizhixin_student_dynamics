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
@Table(name = "T_RULE_PARAMETER")
@ToString
public class  RuleParameter extends AbstractEntity {
    /*
     * 机构id
     */
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;

     /*
      * 规则名称
      */
    @Column(name = "RULE_NAME")
    @Getter @Setter private String ruleName;
    /*
    * 规则描述
    */
    @Column(name = "RULE_DESCRIBE")
    @Getter @Setter private String ruledescribe;
    /*
     * 规则左区间参数值
     */
    @Column(name = "LEFT_PARAMETER")
    @Getter @Setter private String leftParameter;

    /*
     * 规则左区间参数关系(大于,大于等于)
     */
    @Column(name = "LFET_RELATIONSHIP")
    @Getter @Setter private String leftRelationship;

    /*
    * 规则右区间参数值
    */
    @Column(name = "RIGHT_PARAMETER")
    @Getter @Setter private String rightParameter;

    /*
     * 规则右区间参数关系(大于,大于等于)
     */
    @Column(name = "RIGHT_RELATIONSHIP")
    @Getter @Setter private String rightRelationship;

    /*
     * 规则的序号
     */
    @Column(name = "SERIAL_NUMBER")
    @Getter @Setter private int serialNumber;


}
