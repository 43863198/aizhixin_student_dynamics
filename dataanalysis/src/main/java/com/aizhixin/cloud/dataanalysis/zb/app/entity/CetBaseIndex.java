package com.aizhixin.cloud.dataanalysis.zb.app.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ApiModel(description="等级考试基础指标")
@Entity(name = "T_ZB_DJKSJC")
@NoArgsConstructor
@ToString
public class CetBaseIndex {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "XN学年")
    @Column(name = "XN")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "XQM学期")
    @Column(name = "XQM")
    @Getter @Setter private String xqm;
    @ApiModelProperty(value = "XXDM学校代码")
    @Column(name = "XXDM")
    @Getter @Setter private String xxdm;
    @ApiModelProperty(value = "DHLJ单次或者累计 1单次 2累计")
    @Column(name = "DHLJ")
    @Getter @Setter private String dhlj;
    @ApiModelProperty(value = "P_BH上一级编码")
    @Column(name = "P_BH")
    @Getter @Setter private String pbh;
    @ApiModelProperty(value = "编码")
    @Column(name = "BH")
    @Getter @Setter private String bh;
    @ApiModelProperty(value = "KSLX考试类型3三级、4四级、6六级")
    @Column(name = "KSLX")
    @Getter @Setter private String kslx;
    @ApiModelProperty(value = "ZXRS在校人数")
    @Column(name = "ZXRS")
    @Getter @Setter private Long zxrs;
    @ApiModelProperty(value = "NZXRS男在校人数")
    @Column(name = "NZXRS")
    @Getter @Setter private Long nzxrs;
    @ApiModelProperty(value = "VZXRS女在校人数")
    @Column(name = "VZXRS")
    @Getter @Setter private Long vzxrs;
    @ApiModelProperty(value = "CKRC参考人次")
    @Column(name = "CKRC")
    @Getter @Setter private Long ckrc;
    @ApiModelProperty(value = "ZF总分")
    @Column(name = "ZF")
    @Getter @Setter private Double zf;
    @ApiModelProperty(value = "GF最高分")
    @Column(name = "GF")
    @Getter @Setter private Double gf;
    @ApiModelProperty(value = "TGZF通过人员的总分")
    @Column(name = "TGZF")
    @Getter @Setter private Double tgzf;
    @ApiModelProperty(value = "TGRC通过人次")
    @Column(name = "TGRC")
    @Getter @Setter private Long tgrc;
    @ApiModelProperty(value = "NRC男总人次")
    @Column(name = "NRC")
    @Getter @Setter private Long nrc;
    @ApiModelProperty(value = "NZF男总分")
    @Column(name = "NZF")
    @Getter @Setter private Double nzf;
    @ApiModelProperty(value = "VRC女总人次")
    @Column(name = "VRC")
    @Getter @Setter private Long vrc;
    @ApiModelProperty(value = "VZF女总分")
    @Column(name = "VZF")
    @Getter @Setter private Double vzf;
    @ApiModelProperty(value = "NTGRC男通过人次")
    @Column(name = "NTGRC")
    @Getter @Setter private Long ntgrc;
    @ApiModelProperty(value = "VTGRC女通过人次")
    @Column(name = "VTGRC")
    @Getter @Setter private Long vtgrc;
}
