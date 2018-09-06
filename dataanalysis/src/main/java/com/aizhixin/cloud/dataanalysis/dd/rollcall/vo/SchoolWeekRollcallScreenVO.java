package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 学校最新两周考勤统计
 */
@ApiModel
@ToString
@NoArgsConstructor
public class SchoolWeekRollcallScreenVO {
    @ApiModelProperty(value = "日期")
    @Getter  @Setter private String day;
    @ApiModelProperty(value = "星期几(1到7表示周一到周日)")
    @Getter  @Setter private String dayOfWeek;
    @ApiModelProperty(value = "应到人数")
    @Getter  @Setter private Integer ydrs;
    @ApiModelProperty(value = "实到人数")
    @Getter  @Setter private Integer sdrs;
    @ApiModelProperty(value = "迟到人数")
    @Getter  @Setter private Integer cdrs;
    @ApiModelProperty(value = "请假人数")
    @Getter  @Setter private Integer qjrs;
    @ApiModelProperty(value = "旷课人数")
    @Getter  @Setter private Integer kkrs;
    @ApiModelProperty(value = "早退人数")
    @Getter  @Setter private Integer ztrs;
//    @ApiModelProperty(value = "到课率")
//    @Getter  @Setter private String dkl;
//    @ApiModelProperty(value = "趋势%")
//    @Getter  @Setter private String qs;

    public SchoolWeekRollcallScreenVO (String day, Integer ydrs, Integer sdrs, Integer cdrs, Integer qjrs, Integer kkrs, Integer ztrs) {
        this.day = day;
        this.ydrs = ydrs;
        this.sdrs = sdrs;
        this.cdrs = cdrs;
        this.qjrs = qjrs;
        this.kkrs = kkrs;
        this.ztrs = ztrs;
    }
}
