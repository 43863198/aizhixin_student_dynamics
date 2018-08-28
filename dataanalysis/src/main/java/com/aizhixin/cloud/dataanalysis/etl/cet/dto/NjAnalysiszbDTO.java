package com.aizhixin.cloud.dataanalysis.etl.cet.dto;

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
public class NjAnalysiszbDTO {
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
    @ApiModelProperty(value = "年级")
    @Getter @Setter private String nj;
    @ApiModelProperty(value = "在校人数")
    @Getter @Setter private Long zxrs;
    @ApiModelProperty(value = "参考人数")
    @Getter @Setter private Long ckrs;
    @ApiModelProperty(value = "总分")
    @Getter @Setter private Double zf;
    @ApiModelProperty(value = "通过人次")
    @Getter @Setter private Long tgrc;

    public NjAnalysiszbDTO(String xn, String xq, String kslx, String pbh , String bh, String nj, Long ckrc, Double zf, Long tgrc) {
        this.xn = xn;
        this.xq = xq;
        this.kslx = kslx;
        this.pbh = pbh;
        this.bh = bh;
        this.nj = nj;
        this.ckrs = ckrc;
        this.zf = zf;
        this.tgrc = tgrc;
    }

    public NjAnalysiszbDTO(String kslx, String pbh , String bh, String nj, Long ckrc, Double zf, Long tgrc) {
        this(null, null, kslx, pbh, bh, nj, ckrc, zf, tgrc);
    }
}
