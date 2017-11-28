package com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="RollCall")
@Data
public class RollCall {

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
	@Indexed
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
	private Long scheduleId;
	
	/**
	 * 选课名称
	 */
	private String courseName;
	
	/**
	 * 选课类型（必修，限选，任选）
	 */
	private String courseType;
	
	/**
	 * 考勤日期
	 */
	private Date rollCallDate;
	
	/**
	 * 考勤类型
	 */
	private String rollCallType;
	
	/**
	 * 考勤结果
	 */
	@Indexed
	private String rollCallResult;
	
	/**
	 * 备注
	 */
	private String remarks;
	
}
