package com.aizhixin.cloud.dataanalysis.zb.app.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ApiModel(description="学生学期成绩指标")
@Entity(name = "T_ZB_XSXQCJ")
@NoArgsConstructor
@ToString
public class StudentSemesterScoreIndex {
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
    @ApiModelProperty(value = "ZYH专业号")
    @Column(name = "ZYH")
    @Getter @Setter private String zyh;
    @ApiModelProperty(value = "BH班级编码")
    @Column(name = "BH")
    @Getter @Setter private String bh;
    @ApiModelProperty(value = "XH学号")
    @Column(name = "XH")
    @Getter @Setter private String xh;
    @ApiModelProperty(value = "XM姓名")
    @Column(name = "XM")
    @Getter @Setter private String xm;
    @ApiModelProperty(value = "年级")
    @Column(name = "NJ")
    @Getter @Setter private String nj;

    @ApiModelProperty(value = "GPA平均学分绩点")
    @Column(name = "GPA")
    @Getter @Setter private Double gpa;
    @ApiModelProperty(value = "CKKCS参考课程数")
    @Column(name = "CKKCS")
    @Getter @Setter private Long ckkcs;
    @ApiModelProperty(value = "BJGKCS不及格课程数")
    @Column(name = "BJGKCS")
    @Getter @Setter private Long bjgkcs;
    @ApiModelProperty(value = "BJGZXF不及格课程学分总计")
    @Column(name = "BJGZXF")
    @Getter @Setter private Double bjgzxf;

    public StudentSemesterScoreIndex (String yxsh, String zyh, String bh, String xh, String xm, String nj, Double gpa) {
        this.yxsh = yxsh;
        this.zyh = zyh;
        this.bh = bh;
        this.xh = xh;
        this.xm = xm;
        this.nj = nj;
        this.gpa = gpa;
    }
}
