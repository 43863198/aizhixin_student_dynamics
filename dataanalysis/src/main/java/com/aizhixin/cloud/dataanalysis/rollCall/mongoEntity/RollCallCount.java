package com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="RollCallCount")
@Data
public class RollCallCount {

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
	@Indexed
	private int semester;
	
	/**
	 * 学年
	 */
	@Indexed
	private int schoolYear;
	
	
	/**
	 * 旷课次数
	 */
	private int outSchoolTimes;
	
	/**
	 * 迟到次数
	 */
	private int lateTimes;
	
	/**
	 * 请假次数
	 */
	private int leaveTimes;
	
	/**
	 * 备注
	 */
	private String remarks;
	
}
