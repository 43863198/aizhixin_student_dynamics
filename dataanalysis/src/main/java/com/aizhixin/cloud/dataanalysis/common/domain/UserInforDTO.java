package com.aizhixin.cloud.dataanalysis.common.domain;



import lombok.Data;

@Data
public class UserInforDTO implements java.io.Serializable{
	
	
	private String id;
	
	/**
	 * 机构id
	 */
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
	
}