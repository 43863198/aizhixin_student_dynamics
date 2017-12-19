package com.aizhixin.cloud.dataanalysis.analysis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@ApiModel(description="课程评价信息")
@Data
@ToString
public class CourseEvaluateDTO {
    @ApiModelProperty(value = "课程编号", required = false)
    private String courseCode;
    @ApiModelProperty(value = "课程名称", required = false)
    private String courseName;
    @ApiModelProperty(value = "平均分", required = false)
    private float avgScore;
    @ApiModelProperty(value = "统计时间", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date statisticalTime;
}
