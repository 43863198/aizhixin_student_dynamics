package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(description="课程评价信息")
@Data
@ToString
public class TeacherEvaluateDTO {
    @ApiModelProperty(value = "教师id", required = false)
    private String teacherId;
    @ApiModelProperty(value = "教师名称", required = false)
    private String teacherName;
    @ApiModelProperty(value = "平均分", required = false)
    private float avgScore;
}
