package com.aizhixin.cloud.dataanalysis.setup.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-27
 */
@ApiModel(description="预警规则设置信息")
@Data
public class AlarmRuleDomain {

    @ApiModelProperty(value = "id", required = false)
    private String id;

    @ApiModelProperty(value = "规则名称", required = false)
    private String name;

    @ApiModelProperty(value = "参数值", required = false)
    private int parameter;

    @ApiModelProperty(value = "机构id", required = false)
    private Long orgId;


}
