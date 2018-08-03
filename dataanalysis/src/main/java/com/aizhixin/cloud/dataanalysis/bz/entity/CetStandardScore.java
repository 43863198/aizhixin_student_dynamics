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

@ApiModel(description="标准等级考试成绩数据")
@Entity(name = "T_B_DJKSXX")
@NoArgsConstructor
@ToString
public class CetStandardScore {
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
    @ApiModelProperty(value = "XBM性别码")
    @Column(name = "XBM")
    @Getter @Setter private String xbm;
    @ApiModelProperty(value = "NJ年级")
    @Column(name = "NJ")
    @Getter @Setter private String nj;
    @ApiModelProperty(value = "LYQXM来源区县码")
    @Column(name = "LYQXM")
    @Getter @Setter private String lyqxm;
    @ApiModelProperty(value = "LYDQM来源地区码")
    @Column(name = "LYDQM")
    @Getter @Setter private String lydqm;
    @ApiModelProperty(value = "LYGBM来源国别码")
    @Column(name = "LYGBM")
    @Getter @Setter private String lygbm;
    @ApiModelProperty(value = "LYZBM来源洲别码")
    @Column(name = "LYZBM")
    @Getter @Setter private String lyzbm;
    @ApiModelProperty(value = "CSRQ出生日期")
    @Column(name = "CSRQ")
    @Getter @Setter private Date csrq;
    @ApiModelProperty(value = "ZKZH准考证号")
    @Column(name = "ZKZH")
    @Getter @Setter private String zkzh;
    @ApiModelProperty(value = "KSLX考试类型")
    @Column(name = "KSLX")
    @Getter @Setter private String kslx;
    @ApiModelProperty(value = "KSRQ考试日期")
    @Column(name = "KSRQ")
    @Getter @Setter private Date ksrq;
    @ApiModelProperty(value = "BMRQ报名日期")
    @Column(name = "BMRQ")
    @Getter @Setter private Date bmrq;
    @ApiModelProperty(value = "KSSC考试时长")
    @Column(name = "KSSC")
    @Getter @Setter private Double kssc;
    @ApiModelProperty(value = "CJ综合成绩")
    @Column(name = "CJ")
    @Getter @Setter private Double cj;
    @ApiModelProperty(value = "TLCJ听力成绩")
    @Column(name = "TLCJ")
    @Getter @Setter private Double tlcj;
    @ApiModelProperty(value = "YDCJ阅读成绩")
    @Column(name = "YDCJ")
    @Getter @Setter private Double ydcj;
    @ApiModelProperty(value = "XZCJ写作成绩")
    @Column(name = "XZCJ")
    @Getter @Setter private Double xzcj;
    @ApiModelProperty(value = "TG是否通过")
    @Column(name = "TG")
    @Getter @Setter private Integer tg;
    @ApiModelProperty(value = "CK是否参考")
    @Column(name = "CK")
    @Getter @Setter private Integer ck;
}
