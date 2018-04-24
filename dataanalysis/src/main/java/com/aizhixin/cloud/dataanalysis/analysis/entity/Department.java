package com.aizhixin.cloud.dataanalysis.analysis.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-23
 */
@Entity
@Table(name = "T_DEPARTMENT")
@ToString
public class Department implements Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    private Long id;
    /*学校id*/
    @Column(name = "ORG_ID")
    @Getter @Setter
    private Long orgId;
    /*单位号*/
    @Column(name = "COMPANY_NUMBER")
    @Getter @Setter private String companyNumber;
    /*单位名称*/
    @Column(name = "COMPANY_NAME")
    @Getter @Setter private String companyName;
    /*最后更新时间*/
    @Column(name = "LAST_UPDATE_TIME")
    @Getter @Setter private String lastUpdateTime ;

}
