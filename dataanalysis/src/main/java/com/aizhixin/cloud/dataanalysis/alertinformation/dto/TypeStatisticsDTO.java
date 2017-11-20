package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-17
 */
@ApiModel(description="预警分类统计")
@Data
public class TypeStatisticsDTO {

    @ApiModelProperty(value = "类型名称", required = false)
    private String warningType;

    @ApiModelProperty(value = "一级预警数量", required = false)
    protected int sum1;

    @ApiModelProperty(value = "二级预警数量", required = false)
    protected int sum2;

    @ApiModelProperty(value = "三级预警数量", required = false)
    protected int sum3;

}
