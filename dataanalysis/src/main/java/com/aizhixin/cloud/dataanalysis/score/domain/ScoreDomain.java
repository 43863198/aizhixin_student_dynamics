package com.aizhixin.cloud.dataanalysis.score.domain;

import com.aizhixin.cloud.dataanalysis.studentRegister.domain.StudentInfoDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="导入成绩数据")
public class ScoreDomain extends StudentInfoDomain {
	
	
    @ApiModelProperty(value = "semester 学期", position=1)
    @Getter @Setter private String semester;
    
    @ApiModelProperty(value = "schoolYear 学年学期", position=2)
    @Getter @Setter private String schoolYear;
    
    @ApiModelProperty(value = "scheduleId 排课ID", position=3)
    @Getter @Setter private String scheduleId;
    
    @ApiModelProperty(value = "courseType 选课类型", position=4)
    @Getter @Setter private String courseType;
	
    @ApiModelProperty(value = "usualScore 平时成绩", position=5)
    @Getter @Setter private float usualScore;
	
    @ApiModelProperty(value = "credit 所得学分", position=6)
    @Getter @Setter private float credit;
	
    @ApiModelProperty(value = "gradePoint 绩点", position=7)
    @Getter @Setter private float gradePoint;
    
    @ApiModelProperty(value = "examTime 考试时间", position=8)
    @Getter @Setter private String examTime;
    
    @ApiModelProperty(value = "totalScore 课程成绩", position=9)
    @Getter @Setter private float totalScore;
    
    @ApiModelProperty(value = "grade 年级", position=10)
    @Getter @Setter private String grade;
    
	public ScoreDomain() {}

	public ScoreDomain(Integer line, Long orgId, String jobNum, Long userId, String userName, String classCode,
			String className, String professionalCode, String professionalName, String collegeCode, String collegeName,
			String userPhone, String semester, String schoolYear, String scheduleId, String courseType, float usualScore,
                       float credit, float gradePoint, String examTime, float totalScore, String grade) {
		super(line, orgId, jobNum, userId, userName, classCode, className, professionalCode, professionalName, collegeCode,
				collegeName, userPhone);
		this.semester = semester;
		this.schoolYear = schoolYear;
		this.scheduleId = scheduleId;
		this.courseType = courseType;
		this.usualScore = usualScore;
		this.credit = credit;
		this.gradePoint = gradePoint;
		this.examTime = examTime;
		this.totalScore = totalScore;
		this.grade = grade;
	}
	
}
