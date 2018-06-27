package com.aizhixin.cloud.dataanalysis.score.mongoEntity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;

import lombok.Data;

@Document(collection="Score")
@Data
public class Score {

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

	@ApiModelProperty(value = "学期", required = false)
	private String semester;

	@ApiModelProperty(value = "学年", required = false)
	private String teachYear;
	/**
	 * 年级
	 */
	private String grade;

	/**
	 * 排课id
	 */
	private String scheduleId;
	
	/**
	 * 课程名称
	 */
	private String courseName;
	
	/**
	 * 选课类型: require:必修,limite:限选,optional:任选
	 */
	private String courseType =ScoreConstant.REQUIRED_COURSE;
	
	/**
	 * 考试时间
	 */
	private Date examTime;
	
	/**
	 * 期中成绩
	 */
	private float midtermScore;
	
	/**
	 * 期末成绩
	 */
	private float finalScore;
	
	/**
	 * 平时成绩
	 */
	private float usualScore;
	
	/**
	 * 总评成绩
	 */
	private float totalScore;
	
	/**
	 * 成绩类型: course:课程成绩,英语四级:cet4,英语六级:cet6
	 */
	private String examType = ScoreConstant.EXAM_TYPE_COURSE;
	
	
	/**
	 * 成绩结果类型:  100:百分制
	 */
	private String scoreResultType =ScoreConstant.RESULT_TYPE_100;
	
	/**
	 * 所得学分
	 */
	private float credit;
	
	/**
	 * 绩点
	 */
	private float gradePoint;
	
	/**
	 * 备注
	 */
	private String remarks;
	
}
