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
    
    @ApiModelProperty(value = "isRegister 是否报到 0--已报到，1--未报到", position=12)
    @Getter @Setter private int isRegister;
    
    @ApiModelProperty(value = "actualRegisterDate 实际报到日期", position=13)
    @Getter @Setter private String actualRegisterDate;
    
    @ApiModelProperty(value = "schoolYear 学年学期", position=14)
    @Getter @Setter private String schoolYear;
    
    @ApiModelProperty(value = "isPay 是否已缴费", position=15)
    @Getter @Setter private int isPay;
    
    @ApiModelProperty(value = "isGreenChannel 是否有银行卡号", position=16)
    @Getter @Setter private int isGreenChannel;
    
    @ApiModelProperty(value = "id ID", position=19)
    @Getter @Setter private Long id;
    
    @ApiModelProperty(value = "msg 消息获取错误描述", position=20)
    @Getter @Setter private String msg;
    
	public StudentRegisterDomain() {}

	public StudentRegisterDomain(Integer line, String jobNum, String grade, int isRegister, String actualRegisterDate,
			String schoolYear, int isPay, int isGreenChannel) {
		this.line = line;
		this.jobNum = jobNum;
		this.grade = grade;
		this.isRegister = isRegister;
		this.actualRegisterDate = actualRegisterDate;
		this.schoolYear = schoolYear;
		this.isPay = isPay;
		this.isGreenChannel = isGreenChannel;
	}

}
