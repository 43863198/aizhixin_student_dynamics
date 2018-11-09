package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "批量处理本页预警信息")
@Data
public class BatchDealDomain {
    @ApiModelProperty(value = "预警信息id列表", required = false)
    private String[] warningInformationIds;

    @ApiModelProperty(value = "撤销或学院处理意见")
    private String comments;
}
