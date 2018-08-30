package com.aizhixin.cloud.dataanalysis.analysis.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(description="考勤概况统计")
@Data
@ToString
public class AttendanceStatisticsDomain {
    @ApiModelProperty(value = "学生应出勤人次")
    private Integer total;
    @ApiModelProperty(value = "学生实到人次")
    private Integer actual;
    @ApiModelProperty(value = "学生请假人次")
    private Integer leave;
    @ApiModelProperty(value = "学生旷课人次")
    private Integer absentee;
    @ApiModelProperty(value = "平均到课率")
    private Double avg;
}
