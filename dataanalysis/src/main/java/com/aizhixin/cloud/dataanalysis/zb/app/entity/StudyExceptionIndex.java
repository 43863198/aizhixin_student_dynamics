package com.aizhixin.cloud.dataanalysis.zb.app.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ApiModel(description="修读异常指标")
@Entity(name = "T_ZB_XDYC")
@NoArgsConstructor
@ToString
public class StudyExceptionIndex {
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
    @ApiModelProperty(value = "YXSH院系所号")
    @Column(name = "YXSH")
    @Getter @Setter private String yxsh;
    @ApiModelProperty(value = "YXSMC院系所名称")
    @Column(name = "YXSMC")
    @Getter @Setter private String yxsmc;
    @ApiModelProperty(value = "ZYH专业号")
    @Column(name = "ZYH")
    @Getter @Setter private String zyh;
    @ApiModelProperty(value = "ZYMC专业名称")
    @Column(name = "ZYMC")
    @Getter @Setter private String zymc;
    @ApiModelProperty(value = "BJBH班级编码")
    @Column(name = "BJBH")
    @Getter @Setter private String bjbh;
    @ApiModelProperty(value = "BJMC班级名称")
    @Column(name = "BJMC")
    @Getter @Setter private String bjmc;
    @ApiModelProperty(value = "XH学号")
    @Column(name = "XH")
    @Getter @Setter private String xh;
    @ApiModelProperty(value = "XM姓名")
    @Column(name = "XM")
    @Getter @Setter private String xm;
    @ApiModelProperty(value = "kcs课程数")
    @Column(name = "KCS")
    @Getter @Setter private Long kcs;
    @ApiModelProperty(value = "lxkcs漏选课程数")
    @Column(name = "LXKCS")
    @Getter @Setter private Long lxkcs;
    @ApiModelProperty(value = "tgkcs通过课程数")
    @Column(name = "TGKCS")
    @Getter @Setter private Long tgkcs;
    @ApiModelProperty(value = "XF总学分")
    @Column(name = "XF")
    @Getter @Setter private Double xf;
    @ApiModelProperty(value = "LXXF漏选学分")
    @Column(name = "LXXF")
    @Getter @Setter private Double lxxf;
    @ApiModelProperty(value = "TGXF通过学分")
    @Column(name = "TGXF")
    @Getter @Setter private Double tgxf;
    @ApiModelProperty(value = "BXBJGXF必修不及格学分")
    @Column(name = "BXBJGXF")
    @Getter @Setter private Double bxbjgxf;

    @ApiModelProperty(value = "LXKCNR漏选课程内容")
    @Column(name = "LXKCNR")
    @Getter @Setter private String lxkcnr;
    @ApiModelProperty(value = "BXBJGKCNR必修不及格课程内容")
    @Column(name = "BXBJGKCNR")
    @Getter @Setter private String bxbjgkcnr;

    public StudyExceptionIndex (String xh, String xm, String bjmc, String zyh, String zymc, String yxsh, String yxsmc, Long kcs, Long lxkcs, Long tgkcs, Double xf, Double lxxf, Double tgxf, Double bxbjgxf, String lxkcnr, String bxbjgkcnr) {
        this.xh = xh;
        this.xm = xm;
        this.bjmc = bjmc;
        this.zyh = zyh;
        this.zymc = zymc;
        this.yxsh = yxsh;
        this.yxsmc = yxsmc;
        this.kcs = kcs;
        this.lxkcs = lxkcs;
        this.tgkcs = tgkcs;
        this.xf = xf;
        this.lxxf = lxxf;
        this.tgxf = tgxf;
        this.bxbjgxf = bxbjgxf;
        this.lxkcnr = strProcess(lxkcnr);
        this.bxbjgkcnr = strProcess(bxbjgkcnr);
    }

    static String strProcess(String t) {
        if (null != t && !t.isEmpty()) {
            t = t.replaceAll("\\,+", ",");
            t = t.replaceAll("^\\, ", "");
//            t = t.replaceAll("^\\;", "");
            t = t.trim();
            t = t.replaceAll("\\, ", " ");
        }
        return t;
    }
}
