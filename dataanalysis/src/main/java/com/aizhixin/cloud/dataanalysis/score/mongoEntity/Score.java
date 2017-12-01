package com.aizhixin.cloud.dataanalysis.score.mongoEntity;

import java.util.Date;

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
	 * 学院名称
	 */
	private String collegeName;
	
	/**
	 * 用户手机号
	 */
	private  String userPhone;
	
	/**
	 * 年级
	 */
	private String grade;
	
	/**
	 * 入学日期
	 */
	private Date enrollmentate;
	
	/**
	 * 学期
	 */
	private int semester;
	
	/**
	 * 学年
	 */
	private int schoolYear;
	
	/**
	 * 排课id
	 */
	private String scheduleId;
	
	/**
	 * 选课名称
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
	private String midtermScore;
	
	/**
	 * 期末成绩
	 */
	private String finalScore;
	
	/**
	 * 平时成绩
	 */
	private String usualScore;
	
	/**
	 * 总评成绩
	 */
	private String totalScore;
	
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
	private String credit;
	
	/**
	 * 绩点
	 */
	private String gradePoint;
	
	/**
	 * 备注
	 */
	private String remarks;
	
}
