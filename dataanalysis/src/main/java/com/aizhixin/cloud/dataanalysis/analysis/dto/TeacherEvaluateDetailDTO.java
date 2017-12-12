package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(description="课程评价详细信息")
@Data
@ToString
public class TeacherEvaluateDetailDTO {
    @ApiModelProperty(value = "课程名称", required = false)
    private String courseName;
    @ApiModelProperty(value = "班级", required = false)
    private String className;
    @ApiModelProperty(value = "平均分", required = false)
    private float avgScore;
}
