package com.aizhixin.cloud.dataanalysis.etl.score.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 学生成绩指标
 */
@ApiModel("学生成绩指标")
@ToString
@NoArgsConstructor
public class StudentScorezbDTO {
    @ApiModelProperty(value = "学年")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "学学期年")
    @Getter @Setter private String xq;
    @ApiModelProperty(value = "学校代码")
    @Getter @Setter private String xxdm;

    @ApiModelProperty(value = "上级编码")
    @Getter @Setter private String pbh;
    @ApiModelProperty(value = "编码")
    @Getter @Setter private String bh;

    @ApiModelProperty(value = "参考人数")
    @Getter @Setter private Long ckrs;
    @ApiModelProperty(value = "参考人次")
    @Getter @Setter private Long ckrc;
    @ApiModelProperty(value = "必修课参考人次")
    @Getter @Setter private Long bxckrc;
    @ApiModelProperty(value = "必修课不及格人次")
    @Getter @Setter private Long bxbjgrc;
    @ApiModelProperty(value = "参考课程数")
    @Getter @Setter private Long kcs;
    @ApiModelProperty(value = "绩点总分")
    @Getter @Setter private Double jdzf;
    @ApiModelProperty(value = "成绩总分")
    @Getter @Setter private Double cjzf;

    public StudentScorezbDTO(String xn, String xq, String pbh, String bh, Long ckrs, Long ckrc, Long bxckrc, Long bxbjgrc, Long kcs, Double jdzf, Double cjzf) {
        this.xn = xn;
        this.xq = xq;
        this.pbh = pbh;
        this.bh = bh;
        this.ckrs = ckrs;
        this.ckrc = ckrc;
        this.bxckrc = bxckrc;
        this.bxbjgrc = bxbjgrc;
        this.kcs = kcs;
        this.jdzf = jdzf;
        this.cjzf = cjzf;
    }
}
