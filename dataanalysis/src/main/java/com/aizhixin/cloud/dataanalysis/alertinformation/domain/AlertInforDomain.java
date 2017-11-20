/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import java.util.Date;

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
public class AlertInforDomain {

	@ApiModelProperty(value = "id", required = false)
	private String id;
	
	@ApiModelProperty(value = "姓名", required = false)
	private String name;
	
	@ApiModelProperty(value = "学号/工号", required = false)
	private String jobNumber;
	
	@ApiModelProperty(value = "学院名称", required = false)
	private String collogeName;

	@ApiModelProperty(value = "班级名称", required = false)
	private String className;
	
	@ApiModelProperty(value = "预警类型", required = false)
	private String warningType;
	
	@ApiModelProperty(value = "预警等级", required = false)
	private int warningLevel;
	
	@ApiModelProperty(value = "预警时间", required = false)
	private Date warningTime;
}
