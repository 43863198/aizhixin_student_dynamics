package com.aizhixin.cloud.dataanalysis.score.domain;

import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentInfoDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="成绩excel导入信息")
public class ScoreDomain extends StudentInfoDomain {
	
	
    @ApiModelProperty(value = "grade 年级", position=12)
    @Getter @Setter private String grade;
    
    @ApiModelProperty(value = "schoolYear 学年学期", position=13)
    @Getter @Setter private String schoolYear;
    
    @ApiModelProperty(value = "scheduleId 排课ID", position=14)
    @Getter @Setter private String scheduleId;
    
    @ApiModelProperty(value = "courseType 选课类型", position=16)
    @Getter @Setter private String courseType;
	
    @ApiModelProperty(value = "usualScore 平时成绩", position=20)
    @Getter @Setter private String usualScore;
	
    @ApiModelProperty(value = "credit 所得学分", position=24)
    @Getter @Setter private String credit;
	
    @ApiModelProperty(value = "gradePoint 绩点", position=25)
    @Getter @Setter private String gradePoint;
    
    @ApiModelProperty(value = "examTime 考试时间", position=26)
    @Getter @Setter private String examTime;
    
    @ApiModelProperty(value = "totalScore 课程成绩", position=27)
    @Getter @Setter private String totalScore;
    
	public ScoreDomain() {}

	public ScoreDomain(Integer line, Long orgId, String jobNum, Long userId, String userName, Long classId,
			String className, Long professionalId, String professionalName, Long collegeId, String collegeName,
			String grade, String schoolYear, String scheduleId, String courseType, String usualScore, String credit,
			String gradePoint, String examTime, String totalScore) {
		super(line, orgId, jobNum, userId, userName, classId, className, professionalId, professionalName, collegeId,
				collegeName);
		this.grade = grade;
		this.schoolYear = schoolYear;
		this.scheduleId = scheduleId;
		this.courseType = courseType;
		this.usualScore = usualScore;
		this.credit = credit;
		this.gradePoint = gradePoint;
		this.examTime = examTime;
		this.totalScore = totalScore;
	}

	
}