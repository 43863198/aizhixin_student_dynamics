package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-13
 */
@ApiModel(description="英语四六级趋势")
@Data
public class CetTrendDTO {
    @ApiModelProperty(value = "学年", required = false)
    private String year;
    @ApiModelProperty(value = "学期", required = false)
    private String semester;
    @ApiModelProperty(value = "四级通过率", required = false)
    private double cet4PassRate;
    @ApiModelProperty(value = "四级通过率变化", required = false)
    private String cet4Change;
    @ApiModelProperty(value = "六级通过率", required = false)
    private double cet6PassRate;
    @ApiModelProperty(value = "六级通过率变化", required = false)
    private String cet6Change;
}
