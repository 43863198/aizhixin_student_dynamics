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
@ApiModel(description="报到变化")
@Data
@ToString
public class ReportRateVO {
    @ApiModelProperty(value = "学年")
    private String year;
    @ApiModelProperty(value = "报到数")
    private int reportNumber;
    @ApiModelProperty(value = "变化率")
    private double change;

}
