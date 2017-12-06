package com.aizhixin.cloud.dataanalysis.setup.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description="预警设置信息")
@Data
public class WarningTypeDomain {

	private String id;
	
	private Long orgId;

	private String warningType;

	private String warningName;

	private String warningDescribe;

	private int setupCloseFlag;
	
	public WarningTypeDomain(Long orgId){
		this.orgId = orgId;
	}
}
