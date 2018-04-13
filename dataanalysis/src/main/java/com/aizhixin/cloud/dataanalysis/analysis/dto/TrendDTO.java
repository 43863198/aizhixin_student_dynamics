package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-13
 */
@ApiModel(description="趋势")
@Data
public class TrendDTO {
    @ApiModelProperty(value = "学年", required = false)
    private String year;
    @ApiModelProperty(value = "学期", required = false)
    private String semester;
    @ApiModelProperty(value = "值", required = false)
    private String value1;
    @ApiModelProperty(value = "变化率", required = false)
    private double change1;
    @ApiModelProperty(value = "值", required = false)
    private String value2;
    @ApiModelProperty(value = "变化率", required = false)
    private double change2;
    @ApiModelProperty(value = "值", required = false)
    private String value3;
    @ApiModelProperty(value = "变化率", required = false)
    private double change3;
    @ApiModelProperty(value = "值", required = false)
    private String value4;
    @ApiModelProperty(value = "变化率", required = false)
    private double change4;

}
