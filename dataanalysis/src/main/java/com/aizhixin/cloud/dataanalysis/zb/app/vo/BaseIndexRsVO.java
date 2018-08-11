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
public class BaseIndexRsVO {
    @ApiModelProperty(value = "在校人数")
    @Getter @Setter private Long zxrs = 0L;
    @ApiModelProperty(value = "累计通过人数")
    @Getter @Setter private Long tgrs = 0L;
    @ApiModelProperty(value = "参考人次")
    @Getter @Setter private Long ckrc = 0L;
}
