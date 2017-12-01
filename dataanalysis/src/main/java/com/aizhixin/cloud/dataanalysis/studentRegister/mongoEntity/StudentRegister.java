package com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity;

import java.util.Date;

import org.hibernate.annotations.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="StudentRegister")
@Data
public class StudentRegister {

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
	 * 学年学期
	 */
	private int schoolYear;
	
	/**
	 * 是否报到 1--已报到，0--未报到
	 */
	@Indexed
	private int isRegister;
	
	/**
	 * 是否缴费(是否注册) 1--已缴费，0--未缴费
	 */
	@Indexed
	private int isPay;
	
	/**
	 * 是否绿色通道 (已注册，有银行卡号) 1--是，0--否
	 */
	@Indexed
	private int isGreenChannel;
	
	/**
	 * 报到日期
	 */
	private Date registerDate;
	
	/**
	 * 实际报到日期
	 */
	private Date actualRegisterDate;
	
	/**
	 * 备注
	 */
	private String remarks;
	
}
