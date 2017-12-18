package com.aizhixin.cloud.dataanalysis.monitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ApiModel(description="大屏监控学院平均GPA")
@Data
@ToString
@NoArgsConstructor
public class CollegeGpaDTO {
    @ApiModelProperty(value = "学院id", required = false)
    private String collegeId;
    @ApiModelProperty(value = "学院名称", required = false)
    private String collegeName;
    @ApiModelProperty(value = "评价GPA", required = false)
    private float avgGPA;
}
