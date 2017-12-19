package com.aizhixin.cloud.dataanalysis.monitor.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="教学异常统计数据")
public class AbnormalTeachingDomain {
	
	@ApiModelProperty(value = "机构id")
	@Getter @Setter private Long orgId;

    @ApiModelProperty(value = "上课迟到教师人数")
    @Getter @Setter private int lateNum;
    
    @ApiModelProperty(value = "下课早退教师人数")
    @Getter @Setter private int leaveEarlyNum;
    
    @ApiModelProperty(value = "有教学任务教师人数")
    @Getter @Setter private int teachingNum;
    
    @ApiModelProperty(value = "调停课教师人次")
    @Getter @Setter private int stopClassNum;
    
    @ApiModelProperty(value = "更换主讲教师人次")
    @Getter @Setter private int changeLecturerNum;
    
    @ApiModelProperty(value = "统计时间")
    @Getter @Setter private String statisticalTime;
}
