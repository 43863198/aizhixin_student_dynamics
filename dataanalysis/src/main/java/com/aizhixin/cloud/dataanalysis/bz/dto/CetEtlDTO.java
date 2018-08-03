package com.aizhixin.cloud.dataanalysis.bz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 英语等级考试ETL对象
 */
@ApiModel
@ToString
@NoArgsConstructor
public class CetEtlDTO {
    @ApiModelProperty(value = "院系所编号")
    @Getter @Setter private String yxsh;
    @ApiModelProperty(value = "专业编码")
    @Getter @Setter private String zyh;
    @ApiModelProperty(value = "班级编码")
    @Getter @Setter private String bjbm;
    @ApiModelProperty(value = "学号")
    @Getter @Setter private String xh;
    @ApiModelProperty(value = "姓名")
    @Getter @Setter private String xm;
    @ApiModelProperty(value = "性别")
    @Getter @Setter private String xb;
    @ApiModelProperty(value = "年级")
    @Getter @Setter private String nj;
    @ApiModelProperty(value = "身份证号")
    @Getter @Setter private String sfzh;
    @ApiModelProperty(value = "出生日期")
    @Getter @Setter private Date csrq;
    @ApiModelProperty(value = "考试日期")
    @Getter @Setter private String ksrq;
    @ApiModelProperty(value = "考试类型")
    @Getter @Setter private String kslx;
    @ApiModelProperty(value = "成绩")
    @Getter @Setter private Double cj;

    public CetEtlDTO(String yxsh, String zyh, String bjbm, String xh , String xm, String xb, String nj, String sfzh, Date csrq, String ksrq, String kslx, Double cj) {
        this.yxsh = yxsh;
        this.zyh = zyh;
        this.bjbm = bjbm;
        this.xh = xh;
        this.xm = xm;
        this.xb = xb;
        this.nj = nj;
        this.sfzh = sfzh;
        this.ksrq = ksrq;
        this.kslx = kslx;
        this.csrq = csrq;
        this.cj = cj;
    }
}
