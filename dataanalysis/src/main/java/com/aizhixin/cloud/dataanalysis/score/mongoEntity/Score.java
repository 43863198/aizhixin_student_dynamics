package com.aizhixin.cloud.dataanalysis.score.mongoEntity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
	 * 年级
	 */
	private String grade;
	
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
	 * 选课类型
	 */
	private String courseType;
	
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
	 * 成绩类型
	 */
	private String scoreType;
	
	
	/**
	 * 成绩结果类型
	 */
	private String scoreResultType;
	
	/**
	 * 所得学分
	 */
	private int credit;
	
	/**
	 * 绩点
	 */
	private String gradePoint;
	
	/**
	 * 备注
	 */
	private String remarks;
	
}
