package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-30
 */
@ApiModel(description="迎新趋势")
@Data
public class NewTrendDTO {
    @ApiModelProperty(value = "学年", required = false)
    private String year;
//    @ApiModelProperty(value = "学期", required = false)
//    private String semester;
    @ApiModelProperty(value = "新生人数值", required = false)
    private int newStudentsCount;
    @ApiModelProperty(value = "新生人数变化", required = false)
    private double nscChange;
    @ApiModelProperty(value = "报到人数", required = false)
    private int alreadyReport;
    @ApiModelProperty(value = "报到变化", required = false)
    private double arChange;
    @ApiModelProperty(value = "报到率", required = false)
    private double reportRate;
    @ApiModelProperty(value = "报到率变化", required = false)
    private double rrChange;

}
