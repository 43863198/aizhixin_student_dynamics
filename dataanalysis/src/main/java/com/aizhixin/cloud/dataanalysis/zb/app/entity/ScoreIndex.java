package com.aizhixin.cloud.dataanalysis.zb.app.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ApiModel(description="学生成绩的单位统计指标")
@Entity(name = "T_ZB_XSCJ")
@NoArgsConstructor
@ToString
public class ScoreIndex {
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
    @ApiModelProperty(value = "P_BH上一级编码")
    @Column(name = "P_BH")
    @Getter @Setter private String pbh;
    @ApiModelProperty(value = "编码")
    @Column(name = "BH")
    @Getter @Setter private String bh;

    @ApiModelProperty(value = "参考人数")
    @Column(name = "CKRS")
    @Getter @Setter private Long ckrs;

    @ApiModelProperty(value = "参考人次")
    @Column(name = "CKRC")
    @Getter @Setter private Long ckrc;

    @ApiModelProperty(value = "必修课参考人次")
    @Column(name = "BXCKRC")
    @Getter @Setter private Long bxckrc;

    @ApiModelProperty(value = "必修课不及格人次")
    @Column(name = "BXBJGRC")
    @Getter @Setter private Long bxbjgrc;

    @ApiModelProperty(value = "参考课程数")
    @Column(name = "KCS")
    @Getter @Setter private Long kcs;

    @ApiModelProperty(value = "绩点总分")
    @Column(name = "JDZF")
    @Getter @Setter private Double jdzf;

    @ApiModelProperty(value = "成绩总分")
    @Column(name = "CJZF")
    @Getter @Setter private Double cjzf;

}
