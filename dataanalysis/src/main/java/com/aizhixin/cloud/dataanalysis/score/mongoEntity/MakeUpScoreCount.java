package com.aizhixin.cloud.dataanalysis.score.mongoEntity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="MakeUpScoreCount")
@Data
public class MakeUpScoreCount {

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
	 * 用户手机号
	 */
	private  String userPhone;
	
	/**
	 * 不及格课程数
	 */
	private int failCourseNum;
	
	/**
	 * 不及格课程学分
	 */
	private float failCourseCredit;

	/**
	 * 数据源
	 */
	private String dataSource;
	
	
	/**
	 * 备注
	 */
	private String remarks;
	
}
