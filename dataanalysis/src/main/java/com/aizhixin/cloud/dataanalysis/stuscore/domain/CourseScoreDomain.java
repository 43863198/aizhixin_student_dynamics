package com.aizhixin.cloud.dataanalysis.stuscore.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(description = "课程成绩")
@ToString
@Data
public class CourseScoreDomain {
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "学号")
    private String jobNum;
    @ApiModelProperty(value = "学年")
    private Integer teachingYear;
    @ApiModelProperty(value = "选必修")
    private String xksx;
    @ApiModelProperty(value = "课程学分")
    private Float courseCredit;
    @ApiModelProperty(value = "课程名称")
    private String courseName;
    @ApiModelProperty(value = "授课老师")
    private String courseTeacher;
    @ApiModelProperty(value = "等级类成绩")
    private String djlkscj;
    @ApiModelProperty(value = "课程成绩")
    private Float kccj;
    @ApiModelProperty(value = "平时成绩")
    private Float pscj;
    @ApiModelProperty(value = "分数类成绩")
    private Float fslkscj;
    @ApiModelProperty(value = "绩点")
    private Float jd;
}
