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
public class RollcallStatisticsVO {
    @ApiModelProperty(value = "应到人数")
    @Getter  @Setter protected Integer ydrs;
    @ApiModelProperty(value = "实到人数")
    @Getter  @Setter protected Integer sdrs;
    @ApiModelProperty(value = "迟到人数")
    @Getter  @Setter protected Integer cdrs;
    @ApiModelProperty(value = "请假人数")
    @Getter  @Setter protected Integer qjrs;
    @ApiModelProperty(value = "旷课人数")
    @Getter  @Setter protected Integer kkrs;
    @ApiModelProperty(value = "早退人数")
    @Getter  @Setter protected Integer ztrs;
    @ApiModelProperty(value = "到课率")
    @Getter  @Setter protected Double dkl;

    public RollcallStatisticsVO(Integer ydrs, Integer sdrs, Integer cdrs, Integer qjrs, Integer kkrs, Integer ztrs, Double dkl) {
        this.ydrs = ydrs;
        this.sdrs = sdrs;
        this.cdrs = cdrs;
        this.qjrs = qjrs;
        this.kkrs = kkrs;
        this.ztrs = ztrs;
        this.dkl = dkl;
    }
}
