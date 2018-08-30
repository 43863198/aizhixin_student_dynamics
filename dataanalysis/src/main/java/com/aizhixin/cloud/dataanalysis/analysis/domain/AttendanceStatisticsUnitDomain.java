package com.aizhixin.cloud.dataanalysis.analysis.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(description="按部门考勤统计")
@Data
@ToString
public class AttendanceStatisticsUnitDomain {
    @ApiModelProperty(value = "部门编号")
    private String unitCode;
    @ApiModelProperty(value = "部门名称")
    private String unitName;
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
