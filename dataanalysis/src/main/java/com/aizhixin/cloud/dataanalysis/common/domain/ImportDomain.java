package com.aizhixin.cloud.dataanalysis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="导入新学生信息基础数据")
public class ImportDomain {
    @ApiModelProperty(value = "Id", position=1)
    @Getter @Setter protected String id;
    
    @ApiModelProperty(value = "name", position=2)
    @Getter @Setter protected String name;
    
    @ApiModelProperty(value = "line 行号", position=3)
    @Getter @Setter protected Integer line;

	public ImportDomain() {}

	public ImportDomain(String id, String name, Integer line) {
		this.id = id;
		this.name = name;
		this.line = line;
	}
	
}
