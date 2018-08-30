package com.aizhixin.cloud.dataanalysis.analysis.vo;

import com.aizhixin.cloud.dataanalysis.analysis.domain.AttendanceStatisticsDomain;
import com.aizhixin.cloud.dataanalysis.analysis.domain.AttendanceStatisticsUnitDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ApiModel(description="考勤概况统计")
@Data
@ToString
public class AttendanceStatisticsVO {
    @ApiModelProperty(value = "考勤概况统计")
    private AttendanceStatisticsDomain attendanceStatisticsDomain;
    @ApiModelProperty(value = "各部门考勤统计")
    private List<AttendanceStatisticsUnitDomain> attendanceStatisticsUnitDomainList;
}
