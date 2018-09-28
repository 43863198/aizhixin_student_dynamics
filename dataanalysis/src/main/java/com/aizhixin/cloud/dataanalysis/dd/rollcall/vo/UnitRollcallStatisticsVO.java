package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 学校单位考勤统计
 */
@ApiModel
@ToString
@NoArgsConstructor
public class UnitRollcallStatisticsVO extends RollcallStatisticsVO {
    @ApiModelProperty(value = "单位ID")
    @Getter  @Setter private Long unitId;
    @ApiModelProperty(value = "单位名称")
    @Getter  @Setter private String unitName;

    public UnitRollcallStatisticsVO(Long unitId, String unitName, Integer ydrs, Integer sdrs, Integer cdrs, Integer qjrs, Integer kkrs, Integer ztrs, Double dkl) {
        super(ydrs, sdrs, cdrs, qjrs, kkrs, ztrs, dkl);
        this.unitId = unitId;
        this.unitName = unitName;
    }
}
