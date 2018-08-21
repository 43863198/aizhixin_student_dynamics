package com.aizhixin.cloud.dataanalysis.etl.study.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 专业教学计划
 */
@ApiModel
@ToString
@NoArgsConstructor
public class EtlStudyTeachingPlanDTO {
    @ApiModelProperty(value = "学年")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "学期")
    @Getter @Setter private String xqm;
    @ApiModelProperty(value = "专业号")
    @Getter @Setter private String zyh;
    @ApiModelProperty(value = "年级")
    @Getter @Setter private String nj;
    @ApiModelProperty(value = "教学计划号")
    @Getter @Setter private String jhh;
    @ApiModelProperty(value = "数量")
    @Getter @Setter private long ct;


    public EtlStudyTeachingPlanDTO(String xn, String xqm, String zyh, String nj, long ct) {
       this.xn = xn;
       this.xqm = xqm;
       this.zyh = zyh;
       this.nj = nj;
       this.ct = ct;
    }

    public EtlStudyTeachingPlanDTO(String xn, String xqm, String zyh, String nj, String jhh, long ct) {
        this (xn, xqm, zyh, nj, ct);
        this.jhh = jhh;
    }
}
