package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-11
 */
@ApiModel(description="教学成绩")
@Data
public class TeachingAchievementDTO {
    @ApiModelProperty(value = "学生人数", required = false)
    private int studentsNum;
    @ApiModelProperty(value = "平均GPA", required = false)
    private double averageGPA;
    @ApiModelProperty(value = "不及格人数", required = false)
    private int FailNum;
    @ApiModelProperty(value = "开设课程数量", required = false)
    private int coursesNum;
    @ApiModelProperty(value = "课程平均得分", required = false)
    private double coursesAVGScore;
}
