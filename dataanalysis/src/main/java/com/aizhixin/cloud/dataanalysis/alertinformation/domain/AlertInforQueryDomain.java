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
public class AlertInforQueryDomain {

	@ApiModelProperty(value = "学年", required = false)
	private String teacherYear;

	@ApiModelProperty(value = "学期", required = false)
	private String semester;

	@ApiModelProperty(value = "学院code（多个以,分割）", required = false)
	private String collegeCodes;

	@ApiModelProperty(value = "预警类型（多个以,分割）", required = false)
	private String warningTypes;
	
	@ApiModelProperty(value = "预警等级（多个以,分割）", required = false)
	private String warningLevels;
	
	@ApiModelProperty(value = "预警状态(10:告警中；20：已处理；40:取消)（多个以,分割）", required = false)
	private String warningStates;
	
	@ApiModelProperty(value = "姓名/学号/工号", required = false)
	private String keywords;
	
	@ApiModelProperty(value = "机构id", required = false)
	private Long orgId;
	
	@ApiModelProperty(value = "第几页", required = false)
	private Integer pageNumber;
	
	@ApiModelProperty(value = "每页数据的数目", required = false)
	private Integer pageSize;
}
