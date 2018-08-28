package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "批量处理本页预警信息")
@Data
public class BatchDealDomain {
    @ApiModelProperty(value = "预警信息id列表", required = false)
    private String[] warningInformationIds;

    @ApiModelProperty(value = "处理操作id", required = false)
    private String dealId;

    @ApiModelProperty(value = "处理信息", required = false)
    private String dealInfo;

    @ApiModelProperty(value = "处理类型 辅导员处理10 学院处理 20", required = false)
    private int dealType;
}
