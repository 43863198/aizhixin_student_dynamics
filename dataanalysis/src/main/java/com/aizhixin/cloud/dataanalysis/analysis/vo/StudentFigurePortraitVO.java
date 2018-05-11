package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-11
 */
@ApiModel(description="学生画像信息")
@Data
@ToString
public class StudentFigurePortraitVO {
    @ApiModelProperty(value = "学生信息")
    private StudentInfoVO studentIn;
    @ApiModelProperty(value = "课程成绩")
    private CourseScoreVO courseScore;
    @ApiModelProperty(value = "成绩详情")
    private List<ScoreVO> scoreDetail;
    @ApiModelProperty(value = "等级考试")
    private List<GradeExamScoreVO> gradeExam;

}
