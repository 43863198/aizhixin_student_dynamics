package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(description="课程评价详细信息")
@Data
@ToString
public class CourseEvaluateDetailDTO {
    @ApiModelProperty(value = "教学班名称", required = false)
    private String teachingClassName;
    @ApiModelProperty(value = "授课老师", required = false)
    private String teacherName;
    @ApiModelProperty(value = "平均分", required = false)
    private float avgScore;
}
