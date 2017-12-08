package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description="学校人数统计类")
@Data
public class SchoolStatisticsDTO {
	
    @ApiModelProperty(value = "学院id", required = false)
    private Long collegeId;
    @ApiModelProperty(value = "合计数", required = false)
    private Long countNum;
    @ApiModelProperty(value = "用户类型(10学校管理员，20学院管理员，40班级管理员，60老师，70学生)", required = false)
    private int userType;
    @ApiModelProperty(value = "机构id", required = false)
    private Long orgId;
}
