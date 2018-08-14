package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="英语等级考试基础人数")
@NoArgsConstructor
@ToString
public class BaseIndexYearAvgVO implements Comparable<BaseIndexYearAvgVO> {
    @ApiModelProperty(value = "学年学期")
    @Getter @Setter private String xnxq;
    @ApiModelProperty(value = "在校人数")
    @Getter @Setter private Long zxrs = 0L;
    @ApiModelProperty(value = "参考人次")
    @Getter @Setter private Long ckrc = 0L;
    @ApiModelProperty(value = "人员总分")
    @Getter @Setter private Double zf = 0.0;

    public int compareTo(BaseIndexYearAvgVO o) {
        if (null == o || null == o.getXnxq()) {
            return 1;
        }
        if (null == xnxq) {
            return -1;
        }
        return xnxq.compareTo(o.getXnxq());
    }
}
