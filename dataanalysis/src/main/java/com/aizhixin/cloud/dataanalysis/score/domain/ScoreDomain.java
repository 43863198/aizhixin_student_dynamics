package com.aizhixin.cloud.dataanalysis.score.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description="成绩excel导入信息")
public class ScoreDomain {
	@ApiModelProperty(value = "line 行号", position=1)
    @Getter @Setter private Integer line;
	
	@ApiModelProperty(value = "jobNum 学号", position=2)
	@Getter @Setter private String jobNum;
	
    @ApiModelProperty(value = "grade 年级", position=12)
    @Getter @Setter private String grade;
    
    @ApiModelProperty(value = "schoolYear 学年学期", position=13)
    @Getter @Setter private String schoolYear;
    
    @ApiModelProperty(value = "scheduleId 排课ID", position=14)
    @Getter @Setter private Long scheduleId;
    
    @ApiModelProperty(value = "courseName 选课名称", position=15)
    @Getter @Setter private String courseName;
    
    @ApiModelProperty(value = "courseType 选课类型", position=16)
    @Getter @Setter private String courseType;
	
    @ApiModelProperty(value = "examTime 考试时间", position=17)
    @Getter @Setter private String examTime;
	
    @ApiModelProperty(value = "midtermScore 期中成绩", position=18)
    @Getter @Setter private String midtermScore;
	
    @ApiModelProperty(value = "finalScore 期末成绩", position=19)
    @Getter @Setter private String finalScore;
	
    @ApiModelProperty(value = "usualScore 平时成绩", position=20)
    @Getter @Setter private String usualScore;
	
    @ApiModelProperty(value = "totalScore 总评成绩", position=21)
    @Getter @Setter private String totalScore;
	
    @ApiModelProperty(value = "scoreType 成绩类型", position=22)
    @Getter @Setter private String scoreType;
	
    @ApiModelProperty(value = "scoreResultType 成绩结果类型", position=23)
    @Getter @Setter private String scoreResultType;
	
    @ApiModelProperty(value = "credit 所得学分", position=24)
    @Getter @Setter private int credit;
	
    @ApiModelProperty(value = "gradePoint 绩点", position=25)
    @Getter @Setter private String gradePoint;
    
    @ApiModelProperty(value = "id ID", position=29)
    @Getter @Setter private Long id;
    
    @ApiModelProperty(value = "msg 消息获取错误描述", position=30)
    @Getter @Setter private String msg;

	public ScoreDomain() {}

	public ScoreDomain(Integer line, String jobNum, String schoolYear, Long scheduleId, String examTime,
			String usualScore, String gradePoint) {
		this.line = line;
		this.jobNum = jobNum;
		this.schoolYear = schoolYear;
		this.scheduleId = scheduleId;
		this.examTime = examTime;
		this.usualScore = usualScore;
		this.gradePoint = gradePoint;
	}

	
}
