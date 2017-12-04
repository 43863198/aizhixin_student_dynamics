package com.aizhixin.cloud.dataanalysis.analysis.domain;

import java.util.Date;

import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentInfoDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="实践导入信息")
public class PracticeDomain extends StudentInfoDomain {
	
    @ApiModelProperty(value = "semester 学期", position=1)
    @Getter @Setter private int semester;
    
    @ApiModelProperty(value = "schoolYear 学年", position=2)
    @Getter @Setter private int schoolYear;
    
    @ApiModelProperty(value = "companyName 实践企业名称", position=3)
    @Getter @Setter private String companyName;
    
    @ApiModelProperty(value = "companyProvince 实践企业所在省份", position=4)
    @Getter @Setter private String companyProvince;
	
    @ApiModelProperty(value = "companyCity 实践企业所在城市", position=5)
    @Getter @Setter private String companyCity;
	
    @ApiModelProperty(value = "reviewResult 实践任务评审结果", position=6)
    @Getter @Setter private String reviewResult;
	
    @ApiModelProperty(value = "taskCreatedDate 实践任务日期", position=7)
    @Getter @Setter private Date taskCreatedDate;
    
    @ApiModelProperty(value = "grade 年级", position=10)
    @Getter @Setter private String grade;
    
	public PracticeDomain() {}

	public PracticeDomain(Integer line, Long orgId, String jobNum, Long userId, String userName, Long classId,
			String className, Long professionalId, String professionalName, Long collegeId, String collegeName,
			String userPhone, /*int semester,*/ int schoolYear, String companyName, String companyProvince,
			String companyCity, String reviewResult, Date taskCreatedDate, String grade) {
		super(line, orgId, jobNum, userId, userName, classId, className, professionalId, professionalName, collegeId,
				collegeName, userPhone);
//		this.semester = semester;
		this.schoolYear = schoolYear;
		this.companyName = companyName;
		this.companyProvince = companyProvince;
		this.companyCity = companyCity;
		this.reviewResult = reviewResult;
		this.taskCreatedDate = taskCreatedDate;
		this.grade = grade;
	}
	
}
