package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-17
 */
@ApiModel(description="告警统计")
@Data
public class AlarmStatisticsDTO {
    @ApiModelProperty(value = "告警等级", required = false)
    private String warningLevel;

    @ApiModelProperty(value = "数量", required = false)
    private int sum;

    @ApiModelProperty(value = "占比", required = false)
    private String proportion;

}
