package com.aizhixin.cloud.dataanalysis.rollCall.domain;

import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentInfoDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="考勤导入信息")
public class RollCallDomain extends StudentInfoDomain {

    @ApiModelProperty(value = "scheduleId 排课ID", position=1)
    @Getter @Setter private Long scheduleId;
    
    @ApiModelProperty(value = "rollCallDate 考勤日期", position=2)
    @Getter @Setter private String rollCallDate;
    
    @ApiModelProperty(value = "grade 年级", position=3)
    @Getter @Setter private String grade;
    
    @ApiModelProperty(value = "rollCallResult 考勤结果", position=4)
    @Getter @Setter private String rollCallResult;

	public RollCallDomain() {}

	public RollCallDomain(Integer line, Long orgId, String jobNum, Long userId, String userName, Long classId,
			String className, Long professionalId, String professionalName, Long collegeId, String collegeName,
			Long scheduleId, String rollCallDate, String grade, String rollCallResult) {
		super(line, orgId, jobNum, userId, userName, classId, className, professionalId, professionalName, collegeId,
				collegeName);
		this.scheduleId = scheduleId;
		this.rollCallDate = rollCallDate;
		this.grade = grade;
		this.rollCallResult = rollCallResult;
	}

}
