package com.aizhixin.cloud.dataanalysis.score.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import com.aizhixin.cloud.dataanalysis.common.domain.UserInforDTO;

@ApiModel(description = "处理预警信息")
@Data
public class ScoreDomain {

	@ApiModelProperty(value = "用户基本信息", required = false)
	private UserInforDTO userInforDTO = new UserInforDTO();

	@ApiModelProperty(value = "相邻学期", required = false)
	private int firstSemester;

	@ApiModelProperty(value = "相邻学年", required = false)
	private int firstSchoolYear;

	@ApiModelProperty(value = "相邻学期考试课程总数", required = false)
	private int firstTotalCourseNums;

	@ApiModelProperty(value = "相邻学期考试总成绩", required = false)
	private String firstTotalScores;

	@ApiModelProperty(value = "相邻学期总绩点", required = false)
	private String firstTotalGradePoint;

	@ApiModelProperty(value = "相邻学期平均绩点", required = false)
	private String firstAvgradePoint;

	@ApiModelProperty(value = "最近学期", required = false)
	private int secondSemester;

	@ApiModelProperty(value = "最近学年", required = false)
	private int secondSchoolYear;

	@ApiModelProperty(value = "最近学期考试课程总数", required = false)
	private int secondTotalCourseNums;

	@ApiModelProperty(value = "最近学期考试总成绩", required = false)
	private String secondTotalScores;

	@ApiModelProperty(value = "最近学期总绩点", required = false)
	private String secondTotalGradePoint;

	@ApiModelProperty(value = "最近学期平均绩点", required = false)
	private String secondAvgradePoint;

}
