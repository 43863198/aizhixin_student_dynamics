package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Calendar;
import java.util.Date;

@ApiModel(description = "最近一天英语等级考试通过率")
@NoArgsConstructor
@ToString
public class EnglishLevelBigScreenVO {
    @ApiModelProperty(value = "学年(YYYY-CCCC) 秋YYYY 春CCCC")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "学期(1 秋， 2 春)")
    @Getter @Setter private String xq;
    @ApiModelProperty(value = "考试类型(3 三级,4 四级,6 六级)")
    @Getter @Setter private String kslx;
    @ApiModelProperty(value = "在校人数")
    @Getter @Setter private Long zxrs;
    @ApiModelProperty(value = "参考人数")
    @Getter @Setter private Long ckrs;
    @ApiModelProperty(value = "通过人数")
    @Getter @Setter private Long tgrs;

    public EnglishLevelBigScreenVO (String xn, String xq, String kslx, Long zxrs, Long ckrs, Long tgrs) {
//        this.xn = xn;
//        this.xq = xq;
        if ("2".equals(xq)) {
            this.xq = "春";
            if (null != xn) {
                int p = xn.indexOf("-");
                if (p > 0) {
                    this.xn = xn.substring(p + 1);
                }
            }
        } else {
            this.xq = "秋";
            if (null != xn) {
                int p = xn.indexOf("-");
                if (p > 0) {
                    this.xn = xn.substring(0, p);
                }
            }
        }
        this.kslx = kslx;
        this.zxrs = zxrs;
        this.ckrs = ckrs;
        this.tgrs = tgrs;
    }

    public EnglishLevelBigScreenVO (Date ksrq, String kslx, Long ckrs, Long tgrs) {
        Calendar c = Calendar.getInstance();
        c.setTime(ksrq);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        this.kslx = kslx;
        if (month > 7 ||  month <= 2) {
            this.xn = year + "-" + (year + 1);
            this.xq = "1";
        } else {
            this.xn = (year - 1) + "-" + year;
            this.xq = "2";
        }
        this.ckrs = ckrs;
        this.tgrs = tgrs;
    }
}
