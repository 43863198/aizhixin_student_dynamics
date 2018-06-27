package com.aizhixin.cloud.dataanalysis.score.mongoEntity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="FailScoreStatistics")
@Data
public class FailScoreStatistics {

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
	 * 数据源
	 */
	private String dataSource;

	/**
	 * 累计不及格课程数
	 */
	private int failCourseNum;
	
	/**
	 * 累计不及格课程总学分
	 */
	private float failCourseCredit;

	
}
