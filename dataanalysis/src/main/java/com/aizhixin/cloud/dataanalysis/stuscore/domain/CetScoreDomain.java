package com.aizhixin.cloud.dataanalysis.stuscore.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(description = "英语等级考试成绩")
@ToString
@Data
public class CetScoreDomain {
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "学号")
    private String jobNum;
    @ApiModelProperty(value = "学年")
    private Integer teachingYear;
    @ApiModelProperty(value = "学期")
    private String semester;
    @ApiModelProperty(value = "考试时间")
    private String date;
    @ApiModelProperty(value = "考试类型")
    private String type;
    @ApiModelProperty(value = "成绩")
    private String score;
}
