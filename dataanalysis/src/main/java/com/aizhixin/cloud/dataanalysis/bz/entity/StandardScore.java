package com.aizhixin.cloud.dataanalysis.bz.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@ApiModel(description="标准考试成绩数据")
@Entity(name = "T_B_XSCJXX")
@NoArgsConstructor
@ToString
public class StandardScore {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
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
    @ApiModelProperty(value = "NJ年级")
    @Column(name = "NJ")
    @Getter @Setter private String nj;

    @ApiModelProperty(value = "JXBH教学班号")
    @Column(name = "JXBH")
    @Getter @Setter private String jxbh;
    @ApiModelProperty(value = "KCH课程号")
    @Column(name = "KCH")
    @Getter @Setter private String kch;
    @ApiModelProperty(value = "KCLBM课程类别码 1 必修,2 限选,3 任选,4 辅修,5 实践,9 其他")
    @Column(name = "KCLBM")
    @Getter @Setter private String kclbm;
    @ApiModelProperty(value = "KCXZM课程性质码")
    @Column(name = "KCXZM")
    @Getter @Setter private String kcxzm;
    @ApiModelProperty(value = "XF学分")
    @Column(name = "XF")
    @Getter @Setter private Double xf;
    @ApiModelProperty(value = "JSGH任课教师工号")
    @Column(name = "JSGH")
    @Getter @Setter private String jsgh;
    @ApiModelProperty(value = "KSRQ考试日期")
    @Column(name = "KSRQ")
    @Getter @Setter private Date ksrq;
    @ApiModelProperty(value = "KSFSM考试方式码")
    @Column(name = "KSFSM")
    @Getter @Setter private String ksfsm;
    @ApiModelProperty(value = "KSXZM考试性质码")
    @Column(name = "KSXZM")
    @Getter @Setter private String ksxzm;
    @ApiModelProperty(value = "KSLBM考试类别码")
    @Column(name = "KSLBM")
    @Getter @Setter private String kslbm;
    @ApiModelProperty(value = "CJLX成绩类型")
    @Column(name = "CJLX")
    @Getter @Setter private String cjlx;
    @ApiModelProperty(value = "DJCJ等级成绩")
    @Column(name = "DJCJ")
    @Getter @Setter private String djcj;
    @ApiModelProperty(value = "JD绩点")
    @Column(name = "JD")
    @Getter @Setter private Double jd;
    @ApiModelProperty(value = "SJCJ实践成绩")
    @Column(name = "SJCJ")
    @Getter @Setter private Double sjcj;
    @ApiModelProperty(value = "JSCJ上机成绩")
    @Column(name = "JSCJ")
    @Getter @Setter private Double jscj;
    @ApiModelProperty(value = "SYCJ实验成绩")
    @Column(name = "SYCJ")
    @Getter @Setter private Double sycj;
    @ApiModelProperty(value = "KSCJ考试成绩")
    @Column(name = "KSCJ")
    @Getter @Setter private Double kscj;
    @ApiModelProperty(value = "BFCJ百分制成绩")
    @Column(name = "BFCJ")
    @Getter @Setter private Double bfcj;
    @ApiModelProperty(value = "ZGCJ最高成绩")
    @Column(name = "ZGCJ")
    @Getter @Setter private Double zgcj;
    @ApiModelProperty(value = "QZCJ期终成绩")
    @Column(name = "QZCJ")
    @Getter @Setter private Double qzcj;
    @ApiModelProperty(value = "PSCJ平时成绩")
    @Column(name = "PSCJ")
    @Getter @Setter private Double pscj;
    @ApiModelProperty(value = "KSCS考试次数")
    @Column(name = "KSCS")
    @Getter @Setter private Integer kscs;
    @ApiModelProperty(value = "是否及格JG")
    @Column(name = "JG")
    @Getter @Setter private String jg;
}
