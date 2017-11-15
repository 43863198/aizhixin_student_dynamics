package com.aizhixin.cloud.dataanalysis.setup.entity;

import com.mongodb.selector.ServerSelector;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-15
 */
@Entity
@Table(name = "T_WARNING_TYPE")
@ToString
public class WarningType implements Serializable {

    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter
    @Setter
    protected Long id;

    /*
    *  名称
    */
    @NotNull
    @Column(name = "NAME")
    @Getter
    @Setter
    private String name;

}
