package com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
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
	private int rollCallType;
	
	/**
	 * 考勤结果
	 * 1:已到
	 * 2:旷课
	 * 3:迟到
	 * 4:请假
	 * 5:早退
	 * 6:已提交
	 * 7:未提交
	 * 8:超出设定范围
	 * 9:取消本次考勤
	 */
	@Indexed
	private int rollCallResult;
	
	/**
	 * 备注
	 */
	private String remarks;
	
}
