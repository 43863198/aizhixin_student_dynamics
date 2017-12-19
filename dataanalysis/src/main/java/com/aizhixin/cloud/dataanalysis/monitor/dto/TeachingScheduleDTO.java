package com.aizhixin.cloud.dataanalysis.monitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description="当日排课统计类")
@Data
public class TeachingScheduleDTO {
	
    @ApiModelProperty(value = "合计数", required = false)
    private Long countNum;
    @ApiModelProperty(value = "第几节课", required = false)
    private int periodNo;
    @ApiModelProperty(value = "机构id", required = false)
    private Long orgId;
}
