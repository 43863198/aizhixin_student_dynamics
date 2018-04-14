package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.sql.Time;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-14
 */
@ApiModel(description="历年四六级分数均值")
@Data
@ToString
public class CetAvgVO {
    @ApiModelProperty(value = "学年")
    private String year;
    @ApiModelProperty(value = "四级分数均值")
    private double cet4Avg;
    @ApiModelProperty(value = "六级分数均值")
    private double cet6Avg;


}
