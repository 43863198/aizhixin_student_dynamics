package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 当天累计考勤统计
 * 大屏部分
 */
@ApiModel
@ToString
@NoArgsConstructor
public class CurrentDayRollcallStatisticsVO {
    @ApiModelProperty(value = "应签人次")
    @Getter  @Setter private Integer ydrc;
    @ApiModelProperty(value = "实签人次")
    @Getter  @Setter private Integer sdrc;
    @ApiModelProperty(value = "异常人次")
    @Getter  @Setter private Integer ycrc;
    @ApiModelProperty(value = "到课率")
    @Getter  @Setter protected Double dkl;
    @ApiModelProperty(value = "累计班课数量")
    @Getter  @Setter private Integer ljbksl;
    @ApiModelProperty(value = "总班课数量")
    @Getter  @Setter private Integer zbksl;
    @ApiModelProperty(value = "累计学生数量")
    @Getter  @Setter private Integer ljxssl;
    @ApiModelProperty(value = "总学生数量")
    @Getter  @Setter private Integer zxssl;
}