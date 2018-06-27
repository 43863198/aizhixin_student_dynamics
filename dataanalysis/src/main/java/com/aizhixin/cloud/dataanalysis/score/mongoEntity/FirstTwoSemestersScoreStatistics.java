package com.aizhixin.cloud.dataanalysis.score.mongoEntity;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="FirstTwoSemestersStatistics")
@Data
public class FirstTwoSemestersScoreStatistics {

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
	 * 姓名
	 */
	private String userName;
	
	/**
	 * 照片
	 */
	private String userPhoto;
	
	/**
	 * 班号
	 */
	private String classCode;
	/**
	 * 班级名称
	 */
	private String className;
	
	/**
	 * 专业code
	 */
	private String professionalCode;
	
	/**
	 * 专业名称
	 */
	private String professionalName;
	
	/**
	 * 学院code
	 */
	private String collegeCode;

	/**
	 * 学院名称
	 */
	private String collegeName;
	
	/**
	 * 用户手机号
	 */
	private  String userPhone;

	/**
	 * 数据源
	 */
	private String dataSource;

	@ApiModelProperty(value = "学期", required = false)
	private String semester;

	@ApiModelProperty(value = "学年", required = false)
	private String teachYear;

	@ApiModelProperty(value = "上上学期", required = false)
	private String firstSemester;

	@ApiModelProperty(value = "上上学年", required = false)
	private String firstSchoolYear;

	@ApiModelProperty(value = "上上学期考试课程总数", required = false)
	private int firstTotalCourseNums;

	@ApiModelProperty(value = "上上考试总成绩", required = false)
	private float firstTotalScores;

	@ApiModelProperty(value = "上上学期总学分绩点", required = false)
	private float firstTotalGradePoint;

	@ApiModelProperty(value = "上上学期平均学分绩点", required = false)
	private float firstAvgradePoint;

	@ApiModelProperty(value = "上学期", required = false)
	private String secondSemester;

	@ApiModelProperty(value = "上学年", required = false)
	private String secondSchoolYear;

	@ApiModelProperty(value = "上学期考试课程总数", required = false)
	private int secondTotalCourseNums;

	@ApiModelProperty(value = "上学期考试总成绩", required = false)
	private float secondTotalScores;

	@ApiModelProperty(value = "上学期总学分绩点", required = false)
	private float secondTotalGradePoint;

	@ApiModelProperty(value = "上学期平均学分绩点", required = false)
	private float secondAvgradePoint;
}
