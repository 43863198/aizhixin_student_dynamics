package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@ApiModel(description="学院的告警信息")
@Data
public class CollegeWarningInfoDTO {

    @ApiModelProperty(value = "院系名称", required = false)
    protected String collegeName ;

    @ApiModelProperty(value = "院系code", required = false)
    protected String code ;

    @ApiModelProperty(value = "告警总数量", required = false)
    protected int total;

    @ApiModelProperty(value = "告警处理数量", required = false)
    protected int processedNumber;

    @ApiModelProperty(value = "处理率", required = false)
    protected String processedProportion ;

    @ApiModelProperty(value = "一级告警数量", required = false)
    protected int sum1;

    @ApiModelProperty(value = "二级告警数量", required = false)
    protected int sum2;

    @ApiModelProperty(value = "三级告警数量", required = false)
    protected int sum3;

    @ApiModelProperty(value = "一级告警处理数量", required = false)
    protected int processedSum1;

    @ApiModelProperty(value = "二级告警处理数量", required = false)
    protected int processedSum2;

    @ApiModelProperty(value = "三级告警处理数量", required = false)
    protected int processedSum3;

}
