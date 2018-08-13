package com.aizhixin.cloud.dataanalysis.zb.app.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ApiModel(description="等级考试年级分布指标")
@Entity(name = "T_ZB_DJKSNJ")
@NoArgsConstructor
@ToString
public class CetGradeIndex {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Getter @Setter private Long id;

    @ApiModelProperty(value = "XN学年")
    @Column(name = "XN")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "XQM学期")
    @Column(name = "XQM")
    @Getter @Setter private String xqm;
    @ApiModelProperty(value = "XXDM学校代码")
    @Column(name = "XXDM")
    @Getter @Setter private String xxdm;
    @ApiModelProperty(value = "KSLX考试类型3三级、4四级、6六级")
    @Column(name = "KSLX")
    @Getter @Setter private String kslx;
    @ApiModelProperty(value = "DHLJ单次或者累计 1单次 2累计")
    @Column(name = "DHLJ")
    @Getter @Setter private String dhlj;
    @ApiModelProperty(value = "P_BH上一级编码")
    @Column(name = "P_BH")
    @Getter @Setter private String pbh;
    @ApiModelProperty(value = "编码")
    @Column(name = "BH")
    @Getter @Setter private String bh;
    @ApiModelProperty(value = "年级")
    @Column(name = "NJ")
    @Getter @Setter private String nj;
    @ApiModelProperty(value = "ZXRS在校人数")
    @Column(name = "ZXRS")
    @Getter @Setter private Long zxrs;
    @ApiModelProperty(value = "CKRC参考人次")
    @Column(name = "CKRC")
    @Getter @Setter private Long ckrc;
    @ApiModelProperty(value = "ZF总分")
    @Column(name = "ZF")
    @Getter @Setter private Double zf;
    @ApiModelProperty(value = "TGRC通过人次")
    @Column(name = "TGRC")
    @Getter @Setter private Long tgrc;
}
