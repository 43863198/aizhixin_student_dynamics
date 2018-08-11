package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudentByGradeVo {
    @ApiModelProperty(value = "年级")
    private String grade;
    @ApiModelProperty(value = "总人数")
    private int total;
    @ApiModelProperty(value = "在校人数")
    private int in;
    @ApiModelProperty(value = "离校人数")
    private int out;

}
