package com.aizhixin.cloud.dataanalysis.analysis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel(description="课程评价信息")
@Data
@ToString
public class TeacherEvaluateDTO {
    @ApiModelProperty(value = "教师id", required = false)
    private String teacherId;
    @ApiModelProperty(value = "教师名称", required = false)
    private String teacherName;
    @ApiModelProperty(value = "平均分", required = false)
    private float avgScore;
    @ApiModelProperty(value = "统计时间", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date statisticalTime;
}
