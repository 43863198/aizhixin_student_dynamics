package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="单位评价绩点")
@NoArgsConstructor
@ToString
public class ScoreAvgJdVO {
    @ApiModelProperty(value = "单位编码")
    @Getter @Setter private String code;
    @ApiModelProperty(value = "单位名称")
    @Getter @Setter private String name;
    @ApiModelProperty(value = "平均绩点")
    @Getter @Setter private Double avgjd = 0.0;
}
