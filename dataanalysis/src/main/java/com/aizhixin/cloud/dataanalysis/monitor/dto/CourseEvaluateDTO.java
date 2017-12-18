package com.aizhixin.cloud.dataanalysis.monitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ApiModel(description="实时监控大屏--课程综合评分统计")
@Data
@ToString
@NoArgsConstructor
public class CourseEvaluateDTO {
    @ApiModelProperty(value = "必修", required = false)
    private float mustCourseScore;
    @ApiModelProperty(value = "选修", required = false)
    private float selectCourseScore;
    @ApiModelProperty(value = "任选类", required = false)
    private float otherCourseScore;
}
