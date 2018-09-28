package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 学校单位考勤统计
 */
@ApiModel
@ToString
@NoArgsConstructor
public class UnitRollcallStatisticsDOVO {
    @ApiModelProperty(value = "总统计值")
    @Getter  @Setter private RollcallStatisticsVO statistics;
    @ApiModelProperty(value = "分单位统计值")
    @Getter  @Setter private List<UnitRollcallStatisticsVO> unitList;
}
