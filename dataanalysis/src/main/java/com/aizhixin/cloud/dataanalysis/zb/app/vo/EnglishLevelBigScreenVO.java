package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
        this.xn = xn;
        this.xq = xq;
        this.kslx = kslx;
        this.zxrs = zxrs;
        this.ckrs = ckrs;
        this.tgrs = tgrs;
    }
}
