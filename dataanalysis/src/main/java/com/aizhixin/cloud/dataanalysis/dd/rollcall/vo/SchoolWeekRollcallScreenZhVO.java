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
public class SchoolWeekRollcallScreenZhVO {
    @ApiModelProperty(value = "到课率%")
    @Getter  @Setter private String dkl;
    @ApiModelProperty(value = "趋势%")
    @Getter  @Setter private String qs;
    @ApiModelProperty(value = "每天的统计数据")
    @Getter  @Setter private List<SchoolWeekRollcallScreenVO> days;
}
