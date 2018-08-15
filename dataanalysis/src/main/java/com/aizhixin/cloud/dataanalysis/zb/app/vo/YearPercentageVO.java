package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="年度百分比")
@NoArgsConstructor
@ToString
public class YearPercentageVO implements Comparable<YearPercentageVO> {
    @ApiModelProperty(value = "学年学期")
    @Getter @Setter private String xnxq;
    @ApiModelProperty(value = "比率(已经乘100了)")
    @Getter @Setter private Double rate;

    public int compareTo(YearPercentageVO o) {
        if (null == o || null == o.getXnxq()) {
            return 1;
        }
        if (null == xnxq) {
            return -1;
        }
        return xnxq.compareTo(o.getXnxq());
    }
}
