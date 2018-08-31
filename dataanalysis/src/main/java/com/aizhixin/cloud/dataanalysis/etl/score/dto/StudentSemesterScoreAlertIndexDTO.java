package com.aizhixin.cloud.dataanalysis.etl.score.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 学生学期成绩波动预警
 */
@ApiModel("学生学期成绩波动预警")
@ToString
@NoArgsConstructor
public class StudentSemesterScoreAlertIndexDTO {
    @ApiModelProperty(value = "YXSH院系所号")
    @Getter @Setter private String yxsh;
    @ApiModelProperty(value = "YXSMC院系所名称")
    @Getter @Setter private String yxsmc;
    @ApiModelProperty(value = "ZYH专业号")
    @Getter @Setter private String zyh;
    @ApiModelProperty(value = "ZYMC专业名称")
    @Getter @Setter private String zymc;
    @ApiModelProperty(value = "BJBH班级编码")
    @Getter @Setter private String bjbh;
    @ApiModelProperty(value = "BJMC班级名称")
    @Getter @Setter private String bjmc;
    @ApiModelProperty(value = "XH学号")
    @Getter @Setter private String xh;
    @ApiModelProperty(value = "XM姓名")
    @Getter @Setter private String xm;
    @ApiModelProperty(value = "近学年学期")
    @Getter @Setter private String jxnxq;
    @ApiModelProperty(value = "近GPA")
    @Getter @Setter private Double jgpa;
    @ApiModelProperty(value = "远学年学期")
    @Getter @Setter private String yxnxq;
    @ApiModelProperty(value = "远GPA")
    @Getter @Setter private Double ygpa;
    @ApiModelProperty(value = "GPA差值")
    @Getter @Setter private Double gpa;

    public StudentSemesterScoreAlertIndexDTO (String xh, String xm, String bjbh, String bjmc, String zyh, String zymc, String yxsh, String yxsmc, String jxnxq, Double jgpa, String yxnxq, Double ygpa, Double gpa) {
        this.xh = xh;
        this.xm = xm;
        this.bjbh = bjbh;
        this.bjmc = bjmc;
        this.zyh = zyh;
        this.zymc = zymc;
        this.yxsh = yxsh;
        this.yxsmc = yxsmc;
        this.jxnxq = jxnxq;
        this.jgpa = jgpa;
        this.yxnxq = yxnxq;
        this.ygpa = ygpa;
        this.gpa = gpa;
    }
}
