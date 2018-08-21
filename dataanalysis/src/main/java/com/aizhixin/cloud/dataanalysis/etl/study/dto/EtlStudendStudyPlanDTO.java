package com.aizhixin.cloud.dataanalysis.etl.study.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 学修改计划
 */
@ApiModel
@ToString
@NoArgsConstructor
public class EtlStudendStudyPlanDTO {
    @ApiModelProperty(value = "学年")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "学期")
    @Getter @Setter private String xqm;
    @ApiModelProperty(value = "学号")
    @Getter @Setter private String xh;
    @ApiModelProperty(value = "姓名")
    @Getter @Setter private String xm;
    @ApiModelProperty(value = "年级")
    @Getter @Setter private String nj;
    @ApiModelProperty(value = "班级")
    @Getter @Setter private String bjmc;
    @ApiModelProperty(value = "专业号")
    @Getter @Setter private String zyh;
    @ApiModelProperty(value = "学院")
    @Getter @Setter private String yxsh;
    @ApiModelProperty(value = "课程号")
    @Getter @Setter private String kch;
    @ApiModelProperty(value = "课程名称")
    @Getter @Setter private String kcmc;
    @ApiModelProperty(value = "学分")
    @Getter @Setter private Double xf;
    @ApiModelProperty(value = "学校代码")
    @Getter @Setter private String xxdm;

    public EtlStudendStudyPlanDTO(String zyh, String nj) {
        this.zyh = zyh;
        this.nj = nj;
    }

    public EtlStudendStudyPlanDTO(String kch, String kcmc, Double xf) {
        this.kch = kch;
        this.kcmc = kcmc;
        this.xf = xf;
    }

    public EtlStudendStudyPlanDTO(String xh, String xm, String bjmc, String yxsh) {
        this.xh = xh;
        this.xm = xm;
        this.bjmc = bjmc;
        this.yxsh = yxsh;
    }
}
