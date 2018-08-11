package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="英语等级考试人数性别分布")
@NoArgsConstructor
@ToString
public class SexRsVO {
    @ApiModelProperty(value = "男人数情况")
    @Getter @Setter private BaseIndexRsVO man;
    @ApiModelProperty(value = "女人数情况")
    @Getter @Setter private BaseIndexRsVO women;
}
