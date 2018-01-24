package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "课表")
@Data
@EqualsAndHashCode(callSuper = false)
public class PushMessageDTO {

	private Long id;

	private String title;

	private String content;

	private String businessContent;

	private String module;

	private String function;

	private String pushTime;

	private Boolean haveRead;

	private String studentName;

	private String warningType;

	private String warningLevel;
}
