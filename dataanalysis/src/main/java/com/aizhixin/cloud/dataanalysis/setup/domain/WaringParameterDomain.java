package com.aizhixin.cloud.dataanalysis.setup.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-28
 */
@ApiModel(description="预警设置中的规则和参数对应")
@Data
public class WaringParameterDomain {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "序号")
    private int serialNumber;

    @ApiModelProperty(value = "参数")
    private String parameter;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "规则描述")
    private String ruledescribe;


}