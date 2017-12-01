package com.aizhixin.cloud.dataanalysis.rollCall.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="成绩导入")
public class RollCallDomain {

	@ApiModelProperty(value = "line 行号", position=1)
    @Getter @Setter private Integer line;
	
	@ApiModelProperty(value = "orgId 学校ID", position=2)
	@Getter @Setter private Long orgId;
	
    @ApiModelProperty(value = "jobNum 学号（工号）", position=3)
    @Getter @Setter private String jobNum;
    
    @ApiModelProperty(value = "userId 用户ID", position=4)
    @Getter @Setter private Long userId;
    
    @ApiModelProperty(value = "userName 用户名称", position=5)
    @Getter @Setter private String userName;
    
    @ApiModelProperty(value = "classId 班级ID", position=6)
    @Getter @Setter private Long classId;
    
    @ApiModelProperty(value = "className 班级名称", position=7)
    @Getter @Setter private String className;
    
    @ApiModelProperty(value = "professionalId 专业ID", position=8)
    @Getter @Setter private Long professionalId;
    
    @ApiModelProperty(value = "professionalName 专业名称", position=9)
    @Getter @Setter private String professionalName;
    
    @ApiModelProperty(value = "collegeId 学院ID", position=10)
    @Getter @Setter private Long collegeId;
    
    @ApiModelProperty(value = "collegeName 学院名称", position=11)
    @Getter @Setter private String collegeName;
    
    @ApiModelProperty(value = "scheduleId 排课ID", position=12)
    @Getter @Setter private Long scheduleId;
    
    @ApiModelProperty(value = "rollCallDate 考勤日期", position=13)
    @Getter @Setter private String rollCallDate;
    
    @ApiModelProperty(value = "grade 年级", position=14)
    @Getter @Setter private String grade;
    
    @ApiModelProperty(value = "rollCallResult 考勤结果", position=15)
    @Getter @Setter private String rollCallResult;
    
    @ApiModelProperty(value = "id ID", position=19)
    @Getter @Setter private Long id;
    
    @ApiModelProperty(value = "msg 消息获取错误描述", position=20)
    @Getter @Setter private String msg;

	public RollCallDomain() {}

	public RollCallDomain(Integer line, Long orgId, String jobNum, Long userId, String userName, Long classId,
			String className, Long professionalId, String professionalName, Long collegeId, String collegeName,
			Long scheduleId, String rollCallDate, String grade, String rollCallResult) {
		this.line = line;
		this.orgId = orgId;
		this.jobNum = jobNum;
		this.userId = userId;
		this.userName = userName;
		this.classId = classId;
		this.className = className;
		this.professionalId = professionalId;
		this.professionalName = professionalName;
		this.collegeId = collegeId;
		this.collegeName = collegeName;
		this.scheduleId = scheduleId;
		this.rollCallDate = rollCallDate;
		this.grade = grade;
		this.rollCallResult = rollCallResult;
	}
    
}
