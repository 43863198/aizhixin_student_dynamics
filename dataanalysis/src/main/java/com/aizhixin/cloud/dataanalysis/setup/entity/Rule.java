package com.aizhixin.cloud.dataanalysis.setup.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-17
 */
@Entity
@Table(name = "T_RULE")
@ToString
public class  Rule extends AbstractEntity {
    /*
    * 机构id
    */
    @Column(name = "ORG_ID")
    @Getter @Setter private Long orgId;
    /*
    * 规则名称
    */
    @Column(name = "NAME")
    @Getter
    @Setter
    private String name;

    /*
     * 规则描述
     */
    @Column(name = "RULE_DESCRIBE")
    @Getter @Setter private String ruleDescribe;

}

