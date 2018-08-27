package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(description="辅修、二学位数据概况")
@Data
@ToString
public class OverviewVO {
    @ApiModelProperty(value = "部门在校数")
    private int total;
    @ApiModelProperty(value = "部门辅修人数")
    private int fxtotal;
    @ApiModelProperty(value = "部门二学位人数")
    private int exwtotal;
}
