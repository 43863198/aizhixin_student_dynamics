package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="学生成绩单位统计指标")
@NoArgsConstructor
@ToString
public class ScoreDwCountVO {
    @ApiModelProperty(value = "参考人数")
    @Getter @Setter private Long ckrs = 0L;
    @ApiModelProperty(value = "必修课不及格人次")
    @Getter @Setter private Long bxbjgrc = 0L;
    @ApiModelProperty(value = "课程数")
    @Getter @Setter private Long kcs = 0L;
    @ApiModelProperty(value = "评价平均成绩")
    @Getter @Setter private Double avgcj = 0.0;
    @ApiModelProperty(value = "平均绩点")
    @Getter @Setter private Double avgjd = 0.0;

    public ScoreDwCountVO(Long ckrs, Long bxbjgrc, Long kcs, Double avgcj, Double avgjd) {
        this.ckrs = ckrs;
        this.bxbjgrc = bxbjgrc;
        this.kcs = kcs;
        this.avgcj = avgcj;
        this.avgjd = avgjd;
    }
}
