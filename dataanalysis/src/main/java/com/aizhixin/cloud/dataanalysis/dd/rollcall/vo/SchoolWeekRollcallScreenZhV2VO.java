package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 学校最新两周考勤统计
 */
@ApiModel
@ToString
@NoArgsConstructor
public class SchoolWeekRollcallScreenZhV2VO {
    @ApiModelProperty(value = "周数据统计")
    @Getter  @Setter private RollcallStatisticsVO week;
    @ApiModelProperty(value = "趋势%")
    @Getter  @Setter private Double qs;
    @ApiModelProperty(value = "每天的统计数据")
    @Getter  @Setter private List<SchoolWeekRollcallScreenVO> days;
}
