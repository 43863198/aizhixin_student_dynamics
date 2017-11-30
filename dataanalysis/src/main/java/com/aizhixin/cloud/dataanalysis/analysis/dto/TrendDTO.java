package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-30
 */
@ApiModel(description="趋势")
@Data
public class TrendDTO {
    @ApiModelProperty(value = "年", required = false)
    private String yaer;
    @ApiModelProperty(value = "占比", required = false)
    private String value;
}
