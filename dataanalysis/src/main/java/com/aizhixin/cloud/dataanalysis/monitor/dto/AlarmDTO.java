package com.aizhixin.cloud.dataanalysis.monitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ApiModel(description="大屏监控预警处理")
@Data
@ToString
@NoArgsConstructor
public class AlarmDTO {
    @ApiModelProperty(value = "预警数量", required = false)
    private Integer alarmTotal;
    @ApiModelProperty(value = "已处理预警数量", required = false)
    private Integer dealwithTotal;
}
