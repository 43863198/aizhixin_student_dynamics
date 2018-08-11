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
public class SexAvgVO {
    @ApiModelProperty(value = "男分数情况")
    @Getter @Setter private BaseIndexAvgVO man;
    @ApiModelProperty(value = "女分数情况")
    @Getter @Setter private BaseIndexAvgVO women;
}
