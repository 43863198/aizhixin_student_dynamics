package com.aizhixin.cloud.dataanalysis.setup.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import lombok.Data;

import java.util.List;


@ApiModel(description="预警处理设置信息")
@Data
public class ProcessingModeDomain {

    @ApiModelProperty(value = "机构id", required = false)
    private Long orgId;

    @ApiModelProperty(value = "预警类型id", required = false)
    private String warningTypeId;

    @ApiModelProperty(value = "预警处理方式", required = false)
    private List<ProcessingGradeDomain> processingGreadList;


}
