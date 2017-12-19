package com.aizhixin.cloud.dataanalysis.monitor.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="今日排课数据")
public class TeachingScheduleDomain {
	
	@ApiModelProperty(value = "机构id")
	@Getter @Setter private Long orgId;

    @ApiModelProperty(value = "1，2节排课数量")
    @Getter @Setter private int courseNum1;
    
    @ApiModelProperty(value = "3，4节排课数量")
    @Getter @Setter private int courseNum3;
    
    @ApiModelProperty(value = "5，6节排课数量")
    @Getter @Setter private int courseNum5;
    
    @ApiModelProperty(value = "7，8节排课数量")
    @Getter @Setter private int courseNum7;
    
    @ApiModelProperty(value = "9，10节排课数量")
    @Getter @Setter private int courseNum9;
    
    @ApiModelProperty(value = "11，12节排课数量")
    @Getter @Setter private int courseNum11;
    
    @ApiModelProperty(value = "统计时间")
    @Getter @Setter private String statisticalTime;

}
