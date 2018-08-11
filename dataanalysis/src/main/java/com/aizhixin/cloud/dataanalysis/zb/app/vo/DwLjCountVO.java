package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="英语等级考试单位累计统计值")
@NoArgsConstructor
@ToString
public class DwLjCountVO {
    @ApiModelProperty(value = "在校人数")
    @Getter @Setter private Long zxrs = 0L;
    @ApiModelProperty(value = "累计通过人数")
    @Getter @Setter private Long tgrs = 0L;
    @ApiModelProperty(value = "参考人次")
    @Getter @Setter private Long ckrc = 0L;
    @ApiModelProperty(value = "通过人员总分")
    @Getter @Setter private Double tgzf = 0.0;
}
