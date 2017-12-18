package com.aizhixin.cloud.dataanalysis.monitor.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="学生异常出勤统计数据")
public class AbnormalAttendanceDomain {
	
	@ApiModelProperty(value = "机构id")
	@Getter @Setter private Long orgId;

    @ApiModelProperty(value = "迟到人数")
    @Getter @Setter private int lateNum;
    
    @ApiModelProperty(value = "早退人数")
    @Getter @Setter private int leaveEarlyNum;
    
    @ApiModelProperty(value = "旷课人数")
    @Getter @Setter private int absenteeismNum;
    
    @ApiModelProperty(value = "请假人数")
    @Getter @Setter private int leaveNum;
    
    @ApiModelProperty(value = "统计时间")
    @Getter @Setter private String statisticalTime;
    
}
