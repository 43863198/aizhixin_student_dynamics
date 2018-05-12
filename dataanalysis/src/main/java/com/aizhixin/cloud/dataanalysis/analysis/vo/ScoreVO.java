package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-11
 */
@ApiModel(description="成绩详情")
@Data
@ToString
public class ScoreVO {
    @ApiModelProperty(value = "课程号")
    private String courseNumber;
    @ApiModelProperty(value = "课程名")
    private String courseName;
    @ApiModelProperty(value = "授课教师")
    private String teacherName;
    @ApiModelProperty(value = "学分")
    private float credit;
    @ApiModelProperty(value = "课程性质")
    private String corseType;
    @ApiModelProperty(value = "等级类考试成绩")
    private String gradeExamScore;
    @ApiModelProperty(value = "平时成绩")
    private String academicGPA;
    @ApiModelProperty(value = "分数类考试成绩")
    private String fractionalExamScore;;
    @ApiModelProperty(value = "绩点")
    private float gpaPoint;

}
