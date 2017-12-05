/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhen.pan
 *
 */
@ApiModel(description="注册报到预警信息")
@Data
public class RegisterAlertCountDomain {

	@ApiModelProperty(value = "学院id", required = false)
	protected Long collogeId;
	
	@ApiModelProperty(value = "学院名称", required = false)
	protected String collogeName;

	@ApiModelProperty(value = "人数", required = false)
	protected Long countNum;
	
	@ApiModelProperty(value = "预警等级", required = false)
	protected int warningLevel;
}
