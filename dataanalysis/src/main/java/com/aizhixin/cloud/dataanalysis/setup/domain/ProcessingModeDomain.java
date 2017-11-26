package com.aizhixin.cloud.dataanalysis.setup.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import lombok.Data;


@ApiModel(description="预警处理设置信息")
@Data
public class ProcessingModeDomain {

	@ApiModelProperty(value = "预警设置id", required = false)
    private String alarmSettingsId;

	@ApiModelProperty(value = "预警处理操作类型集合(短信通知，辅导员和学生面谈，院系教务和家长电话联系等)", required = false)
    private String operationTypeSet;

	@ApiModelProperty(value = "机构id", required = false)
    private Long orgId;
}
