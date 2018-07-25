package com.aizhixin.cloud.dataanalysis.analysis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-11
 */
@ApiModel(description="教学成绩")
@Data
@NoArgsConstructor
public class TeachingAchievementDTO {
    @ApiModelProperty(value = "学生人数")
    private int studentsNum;
    @ApiModelProperty(value = "参考学生人次")
    private int scoreNum;
    @ApiModelProperty(value = "必修课人次")
    private int mustNum;
    @ApiModelProperty(value = "必修课不及格人次")
    private int mustFailNum;
    @ApiModelProperty(value = "开设课程数量")
    private int coursesNum;
    @ApiModelProperty(value = "平均GPA")
    private double averageGPA;
    @ApiModelProperty(value = "课程平均得分")
    private double coursesAVGScore;

    @ApiModelProperty(value = "统计时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date statisticalTime = new Date();
}
