package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="预警等级统计数量")
@Data
public class LevelAlertCountDomain {

	@ApiModelProperty(value = "1级预警统计数量", required = false)
	private Long level1CountNum = 0L;
	
	@ApiModelProperty(value = "2级预警统计数量", required = false)
	private Long level2CountNum = 0L;
	
	@ApiModelProperty(value = "3级预警统计数量", required = false)
	private Long level3CountNum = 0L;
}
