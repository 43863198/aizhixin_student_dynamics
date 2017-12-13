package com.aizhixin.cloud.dataanalysis.analysis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

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
    @ApiModelProperty(value = "统计时间", required = false)
    @CreatedDate
    @Column(name = "STATISTICAL_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date statisticalTime = new Date();
}
