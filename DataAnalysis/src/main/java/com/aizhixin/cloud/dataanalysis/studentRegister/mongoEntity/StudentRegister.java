package com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity;

import java.util.Date;

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
	 * 学生名称
	 */
	private String stuName;
	
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
	private String schoolYear;
	
	/**
	 * 是否报到 0--已报到，1--未报到
	 */
	private int isregister;
	
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
