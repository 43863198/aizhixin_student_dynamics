package com.aizhixin.cloud.dataanalysis.openup.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OpenUpInfoDomain {
    private String id;
    @ApiModelProperty(value = "学校id")
    private Long orgId;
    @ApiModelProperty(value = "学校代码")
    private String orgCode;
}
