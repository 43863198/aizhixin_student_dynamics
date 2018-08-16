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
public class ScoreAllYearIndexVO {
    @ApiModelProperty(value = "学年")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "学期")
    @Getter @Setter private String xq;
    @ApiModelProperty(value = "单位编码")
    @Getter @Setter private String code;
    @ApiModelProperty(value = "单位名称")
    @Getter @Setter private String name;
    @ApiModelProperty(value = "参考人次")
    @Getter @Setter private Long ckrc = 0L;
    @ApiModelProperty(value = "必修课不及格人次")
    @Getter @Setter private Long bxbjgrc = 0L;
    @ApiModelProperty(value = "评价平均成绩")
    @Getter @Setter private Double avgcj = 0.0;
    @ApiModelProperty(value = "平均绩点")
    @Getter @Setter private Double avgjd = 0.0;

    public ScoreAllYearIndexVO (String xn, String xq, String code, Long ckrc, Long bxbjgrc, Double avgcj, Double avgjd) {
        this.xn = xn;
        this.xq = xq;
        this.code = code;
        this.ckrc = ckrc;
        this.bxbjgrc = bxbjgrc;
        this.avgcj = avgcj;
        this.avgjd = avgjd;
    }
}
