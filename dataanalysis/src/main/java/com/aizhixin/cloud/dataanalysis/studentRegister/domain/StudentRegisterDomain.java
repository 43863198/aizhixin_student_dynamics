package com.aizhixin.cloud.dataanalysis.studentRegister.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="新生报到excel导入信息")
public class StudentRegisterDomain {
	@ApiModelProperty(value = "line 行号", position=1)
    @Getter @Setter private Integer line;
	
	@ApiModelProperty(value = "jobNum 学号（工号）", position=2)
    @Getter @Setter private String jobNum;
	
    @ApiModelProperty(value = "grade 年级", position=9)
    @Getter @Setter private String grade;
    
    @ApiModelProperty(value = "isregister 是否报到 0--已报到，1--未报到", position=12)
    @Getter @Setter private int isregister;
    
    @ApiModelProperty(value = "actualRegisterDate 实际报到日期", position=13)
    @Getter @Setter private String actualRegisterDate;
    
    @ApiModelProperty(value = "schoolYear 学年学期", position=14)
    @Getter @Setter private String schoolYear;
    
    @ApiModelProperty(value = "id ID", position=19)
    @Getter @Setter private Long id;
    
    @ApiModelProperty(value = "msg 消息获取错误描述", position=20)
    @Getter @Setter private String msg;
    
	public StudentRegisterDomain() {}

	public StudentRegisterDomain(Integer line, String jobNum, String grade, int isregister, String actualRegisterDate,
			String schoolYear) {
		this.line = line;
		this.jobNum = jobNum;
		this.grade = grade;
		this.isregister = isregister;
		this.actualRegisterDate = actualRegisterDate;
		this.schoolYear = schoolYear;
	}

}