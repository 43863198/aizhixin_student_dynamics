package com.aizhixin.cloud.dataanalysis.zb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 基础指标
 */
@ApiModel
@ToString
@NoArgsConstructor
public class AnalysisBasezbDTO {
    @ApiModelProperty(value = "学年")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "学学期年")
    @Getter @Setter private String xq;
    @ApiModelProperty(value = "学校代码")
    @Getter @Setter private String xxdm;
    @ApiModelProperty(value = "考试类型")
    @Getter @Setter private String kslx;
    @ApiModelProperty(value = "单次或者累计")
    @Getter @Setter private String dhlj;
    @ApiModelProperty(value = "上级编码")
    @Getter @Setter private String pbh;
    @ApiModelProperty(value = "编码")
    @Getter @Setter private String bh;
    @ApiModelProperty(value = "在校人数")
    @Getter @Setter private Long zxrs;
    @ApiModelProperty(value = "参考人数")
    @Getter @Setter private Long ckrs;
    @ApiModelProperty(value = "总分")
    @Getter @Setter private Double zf;
    @ApiModelProperty(value = "最高分")
    @Getter @Setter private Double gf;
    @ApiModelProperty(value = "通过总分")
    @Getter @Setter private Double tgzf;
    @ApiModelProperty(value = "通过人次")
    @Getter @Setter private Long tgrc;
    @ApiModelProperty(value = "男总分")
    @Getter @Setter private Double nzf;
    @ApiModelProperty(value = "男人次")
    @Getter @Setter private Long nrc;
    @ApiModelProperty(value = "女总分")
    @Getter @Setter private Double vzf;
    @ApiModelProperty(value = "女人次")
    @Getter @Setter private Long vrc;
    @ApiModelProperty(value = "男通过人次")
    @Getter @Setter private Long ntgrc;
    @ApiModelProperty(value = "女通过人次")
    @Getter @Setter private Long vtgrc;

    public AnalysisBasezbDTO (String xn, String xq, String kslx, String pbh , String bh, Long ckrc, Double zf, Double gf, Long tgrc, Long nrc, Double nzf, Long vrc, Double vzf, Long ntgrc, Long vtgrc) {
        this.xn = xn;
        this.xq = xq;
        this.kslx = kslx;
        this.pbh = pbh;
        this.bh = bh;
        this.ckrs = ckrc;
        this.zf = zf;
        this.gf = gf;
        this.tgrc = tgrc;
        this.nrc = nrc;
        this.nzf = nzf;
        this.vrc = vrc;
        this.vzf = vzf;
        this.ntgrc = ntgrc;
        this.vtgrc = vtgrc;
    }
    public AnalysisBasezbDTO (String xn, String xq, String kslx, String pbh , String bh, Long ckrc, Double zf, Double gf, Double tgzf, Long tgrc, Long nrc, Double nzf, Long vrc, Double vzf, Long ntgrc, Long vtgrc) {
        this(xn, xq, kslx, pbh, bh,ckrc, zf, gf, tgrc, nrc, nzf, vrc, vzf, ntgrc, vtgrc);
        this.tgzf = tgzf;
    }
}
