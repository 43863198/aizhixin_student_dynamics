package com.aizhixin.cloud.dataanalysis.score.mongoEntity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="TotalScoreCount")
@Data
public class TotalScoreCount {

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
	 * 不及格课程数
	 */
	private int failCourseNum;
	
	/**
	 * 不及格必修课程数
	 */
	private int failRequiredCourseNum;
	
	/**
	 * 不及格必修课程学分总计
	 */
	private float requireCreditCount;
	
	
	/**
	 * 备注
	 */
	private String remarks;
	
}
