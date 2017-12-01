package com.aizhixin.cloud.dataanalysis.score.mongoEntity;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="ScoreFluctuateCount")
@Data
public class ScoreFluctuateCount {

	@Id
	private String id;
	
	/**
	 * 机构id
	 */
	@Indexed
	private Long orgId;

	
	/**
	 * 工号/学号
	 */
	private String jobNum;
	
	/**
	 * 用户id
	 */
	private Long userId;
	
	/**
	 * 姓名
	 */
	private String userName;
	
	/**
	 * 照片
	 */
	private String userPhoto;
	
	/**
	 * 班级id
	 */
	private Long classId;
	/**
	 * 班级名称
	 */
	private String className;
	
	/**
	 * 专业id
	 */
	private Long professionalId;
	
	/**
	 * 专业名称
	 */
	private String professionalName;
	
	/**
	 * 学院id
	 */
	private Long collegeId;
	
	/**
	 * 用户手机号
	 */
	private  String userPhone;
	
	/**
	 * 学院名称
	 */
	private String collegeName;
	
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
	
	private String remarks;
	
}
