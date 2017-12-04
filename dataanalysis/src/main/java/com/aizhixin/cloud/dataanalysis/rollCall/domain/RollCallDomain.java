package com.aizhixin.cloud.dataanalysis.rollCall.domain;

import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentInfoDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="导入考勤数据")
public class RollCallDomain extends StudentInfoDomain {

    @ApiModelProperty(value = "scheduleId 排课ID", position=1)
    @Getter @Setter private Long scheduleId;
    
    @ApiModelProperty(value = "rollCallDate 考勤日期", position=2)
    @Getter @Setter private String rollCallDate;
    
    @ApiModelProperty(value = "schoolYear 学年", position=3)
    @Getter @Setter private int schoolYear;
    
    @ApiModelProperty(value = "rollCallResult 考勤结果", position=4)
    @Getter @Setter private String rollCallResult;

	public RollCallDomain() {}

	public RollCallDomain(Integer line, Long orgId, String jobNum, Long userId, String userName, Long classId,
			String className, Long professionalId, String professionalName, Long collegeId, String collegeName,
			String userPhone, Long scheduleId, String rollCallDate, int schoolYear, String rollCallResult) {
		super(line, orgId, jobNum, userId, userName, classId, className, professionalId, professionalName, collegeId,
				collegeName, userPhone);
		this.scheduleId = scheduleId;
		this.rollCallDate = rollCallDate;
		this.schoolYear = schoolYear;
		this.rollCallResult = rollCallResult;
	}

}
