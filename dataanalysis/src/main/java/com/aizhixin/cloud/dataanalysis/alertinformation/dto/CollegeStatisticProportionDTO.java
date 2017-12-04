package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-04
 */
@ApiModel(description="按院统计数量和占比")
@Data
public class CollegeStatisticProportionDTO {

    @ApiModelProperty(value = "院系名称", required = false)
    protected String collegeName ;

    @ApiModelProperty(value = "一级告警数量", required = false)
    protected int sum1;

    @ApiModelProperty(value = "占比", required = false)
    private String proportion1;

    @ApiModelProperty(value = "二级告警数量", required = false)
    protected int sum2;

    @ApiModelProperty(value = "占比", required = false)
    private String proportion2;

    @ApiModelProperty(value = "三级告警数量", required = false)
    protected int sum3;

    @ApiModelProperty(value = "占比", required = false)
    private String proportion3;



}
