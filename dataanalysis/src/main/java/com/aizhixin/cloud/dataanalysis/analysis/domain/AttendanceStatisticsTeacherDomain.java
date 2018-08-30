package com.aizhixin.cloud.dataanalysis.analysis.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(description="按教师考勤统计")
@Data
@ToString
public class AttendanceStatisticsTeacherDomain {
    @ApiModelProperty(value = "学生应出勤人次")
    private Integer total;
    @ApiModelProperty(value = "学生实到人次")
    private Integer arrived;
    @ApiModelProperty(value = "学生迟到人次")
    private Integer late;
    @ApiModelProperty(value = "学生请假人次")
    private Integer leave;
    @ApiModelProperty(value = "学生旷课人次")
    private Integer absentee;
    @ApiModelProperty(value = "学生早退人次")
    private Integer leave_early;
    @ApiModelProperty(value = "平均到课率")
    private Double avg;
    @ApiModelProperty(value = "课程名称")
    private String kcmc;
    @ApiModelProperty(value = "教师工号")
    private String jsgh;
    @ApiModelProperty(value = "授课老师")
    private String skls;
    @ApiModelProperty(value = "院系所号")
    private String yxsh;
    @ApiModelProperty(value = "院系名称")
    private String yxmc;
}
